#!/usr/bin/env python3
import json
import zipfile
from datetime import datetime, timezone
from pathlib import Path
from xml.sax.saxutils import escape


BASE_API = "${baseApiUri}"
SUITE_NAME = "FoodHub Swagger API Test Suite"


HEADERS = [
    "Test Case ID",
    "Module",
    "Endpoint",
    "Method",
    "Scenario",
    "Priority",
    "Preconditions",
    "Headers",
    "Request Payload",
    "Test Data ID",
    "Steps",
    "Expected Status Code",
    "Expected Response / Result",
    "Validations",
    "Edge Case",
    "Automation Notes",
]

DATA_HEADERS = [
    "Test Data ID",
    "Endpoint",
    "Purpose",
    "Headers",
    "Payload",
    "Expected Status Code",
    "Expected Validation",
    "Notes",
]

IMPORT_HEADERS = [
    "Test Suite",
    "Test Case",
    "Test Step",
    "Hit Request",
    "Request Payload",
    "Captured Variables",
    "API_FIELD_VALIDATION",
    "Variable Dependencies",
    "JSON_COMPARE",
    "DB_VALIDATION",
    "DB_CONNECTION",
    "DB_QUERY",
    "API_DB_VALIDATION",
    "DB_COLUMN_VALIDATION",
    "WEB_TEST",
    "PERFORMANCE_TEST",
    "Run",
    "Execution Mode",
    "Status",
]


def compact_json(value):
    return json.dumps(value, separators=(",", ":"))


def hit(endpoint, method, headers="Accept: application/json"):
    return compact_json(
        {
            "endpoint": f"{BASE_API}{endpoint}",
            "method": method,
            "headersText": headers,
        }
    )


def validation(json_path, type_name, expected="", null_validation="Not Null"):
    return {
        "nullValidation": null_validation,
        "jsonPath": json_path,
        "typeValidation": type_name,
        "expectedValueOrVariable": expected,
    }


def row(
    case_id,
    module,
    endpoint,
    method,
    scenario,
    priority,
    preconditions,
    headers,
    payload,
    data_id,
    steps,
    expected_status,
    expected_result,
    validations,
    edge_case,
    notes,
):
    return [
        case_id,
        module,
        endpoint,
        method,
        scenario,
        priority,
        preconditions,
        headers,
        payload if isinstance(payload, str) else compact_json(payload) if payload else "",
        data_id,
        steps,
        expected_status,
        expected_result,
        validations,
        "Yes" if edge_case else "No",
        notes,
    ]


def data(data_id, endpoint, purpose, headers, payload, expected_status, expected_validation, notes):
    return [
        data_id,
        endpoint,
        purpose,
        headers,
        payload if isinstance(payload, str) else compact_json(payload) if payload else "",
        expected_status,
        expected_validation,
        notes,
    ]


def build_cases():
    json_header = "Content-Type: application/json"
    accept_header = "Accept: application/json"
    valid_single = {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "API Test User"}
    valid_multi = {
        "items": [{"menuItemId": 1, "quantity": 2}, {"menuItemId": 2, "quantity": 1}],
        "customerName": "API Test User",
    }

    cases = [
        row("FH-API-001", "Health", "/health", "GET", "Verify health endpoint returns service status.", "P0", "API is deployed.", accept_header, "", "TD-HEALTH-001", "Send GET /health.", "200", "Response contains status value indicating healthy service.", "$.status is present and is a string; response time < 2 seconds.", False, "Existing suite observed $.status value as ok."),
        row("FH-API-002", "Health", "/health", "GET", "Verify health response content type is JSON.", "P1", "API is deployed.", accept_header, "", "TD-HEALTH-001", "Send GET /health with Accept application/json.", "200", "Content-Type is application/json or compatible.", "Header Content-Type contains application/json.", False, "Use header assertion in API client."),
        row("FH-API-003", "Health", "/health", "POST", "Reject unsupported method on health endpoint.", "P1", "API is deployed.", json_header, {}, "TD-HEALTH-002", "Send POST /health.", "405 or 404", "API rejects unsupported method without creating data.", "No 2xx response; error body is JSON if provided.", True, "Accept 404 if the framework routes only registered methods."),
        row("FH-API-004", "Health", "/health", "GET", "Ignore unexpected query parameters on health endpoint.", "P2", "API is deployed.", accept_header, "", "TD-HEALTH-003", "Send GET /health?probe=true&x=<script>.", "200", "Health result is unchanged and no script text is reflected unsafely.", "$.status remains present.", True, "Useful for basic reflected input checks."),
        row("FH-API-005", "Health", "/health", "GET", "Verify health endpoint does not require authentication.", "P1", "API is deployed.", "Accept: application/json\nAuthorization: Bearer invalid-token", "", "TD-HEALTH-004", "Send GET /health with invalid bearer token.", "200", "Health check remains public or ignores invalid auth.", "$.status is present.", True, "If health is intentionally protected, expected code should be 401."),
        row("FH-API-006", "Health", "/health", "GET", "Verify health endpoint performance under repeated calls.", "P2", "API is deployed.", accept_header, "", "TD-HEALTH-005", "Send 20 sequential GET /health requests.", "200 for each", "No intermittent failures.", "95th percentile response time < 2 seconds.", True, "Can be implemented as a lightweight performance test."),

        row("FH-API-007", "Menu", "/menu", "GET", "Retrieve full menu successfully.", "P0", "Menu data exists.", accept_header, "", "TD-MENU-001", "Send GET /menu.", "200", "Response returns a non-empty menu collection.", "Body is an array or contains a menu array; each item has id, name, and price.", False, "Prior reports indicate id, name, price fields."),
        row("FH-API-008", "Menu", "/menu", "GET", "Validate menu item schema and types.", "P0", "Menu data exists.", accept_header, "", "TD-MENU-001", "Send GET /menu and inspect first item.", "200", "First menu item has valid schema.", "id is number or string; name is non-empty string; price is number.", False, "Swagger should be treated as source of truth for id type if available."),
        row("FH-API-009", "Menu", "/menu", "GET", "Validate menu item prices are positive.", "P1", "Menu data exists.", accept_header, "", "TD-MENU-001", "Send GET /menu and inspect all items.", "200", "All prices are greater than 0.", "price > 0 and not null for every item.", True, "Catches free, negative, null, and string prices."),
        row("FH-API-010", "Menu", "/menu", "GET", "Validate menu item IDs are unique.", "P1", "Menu data exists.", accept_header, "", "TD-MENU-001", "Send GET /menu and collect all ids.", "200", "No duplicate item IDs.", "count(distinct id) equals item count.", True, "Important for order item references."),
        row("FH-API-011", "Menu", "/menu", "GET", "Validate menu item names are usable.", "P2", "Menu data exists.", accept_header, "", "TD-MENU-001", "Send GET /menu and inspect names.", "200", "Names are not blank and contain no raw HTML/script.", "trim(name).length > 0; name does not contain <script>.", True, "Basic data quality and XSS guard."),
        row("FH-API-012", "Menu", "/menu", "POST", "Reject unsupported method on menu endpoint.", "P1", "API is deployed.", json_header, {"name": "Injected Item", "price": 0.01}, "TD-MENU-002", "Send POST /menu with a sample item.", "405 or 404", "API rejects menu mutation through public endpoint.", "No 2xx response.", True, "Accept 404 if method-specific route is not defined."),
        row("FH-API-013", "Menu", "/menu", "GET", "Handle unexpected query parameters safely.", "P2", "API is deployed.", accept_header, "", "TD-MENU-003", "Send GET /menu?category=../../etc/passwd&sort=<script>.", "200 or 400", "No server error and no unsafe reflection.", "Status is not 5xx; response body is valid JSON.", True, "If query filtering is unsupported, 200 with normal menu is acceptable."),
        row("FH-API-014", "Menu", "/menu", "GET", "Validate response when Accept is wildcard.", "P3", "API is deployed.", "Accept: */*", "", "TD-MENU-004", "Send GET /menu with Accept */*.", "200", "Menu is returned successfully.", "Response body remains JSON-compatible.", True, "Confirms clients without explicit Accept header still work."),
        row("FH-API-015", "Menu", "/menu", "GET", "Reject or safely handle unsupported Accept header.", "P3", "API is deployed.", "Accept: text/html", "", "TD-MENU-005", "Send GET /menu with Accept text/html.", "200 or 406", "No 5xx response.", "If 200, body must not be malformed.", True, "Implementation may ignore Accept header."),

        row("FH-API-016", "Order", "/order", "POST", "Create order with one valid menu item.", "P0", "Menu item id 1 exists.", json_header, valid_single, "TD-ORDER-001", "Send POST /order with one item quantity 1.", "200 or 201", "Order is created.", "Response has id or orderId; total is numeric and > 0.", False, "Existing reports observed id/total and orderId/total variants."),
        row("FH-API-017", "Order", "/order", "POST", "Create order with multiple valid menu items.", "P0", "Menu item ids 1 and 2 exist.", json_header, valid_multi, "TD-ORDER-002", "Send POST /order with two line items.", "200 or 201", "Order total equals sum of item prices times quantities.", "Response id/orderId present; total numeric.", False, "Compare total using GET /menu prices when automating."),
        row("FH-API-018", "Order", "/order", "POST", "Create order with maximum reasonable quantity.", "P1", "Menu item id 1 exists.", json_header, {"items": [{"menuItemId": 1, "quantity": 99}], "customerName": "Bulk Test"}, "TD-ORDER-003", "Send POST /order with quantity 99.", "200, 201, or 400", "API either accepts bounded quantity or rejects with validation message.", "No 5xx; if accepted, total is correct.", True, "Use Swagger max constraints if defined."),
        row("FH-API-019", "Order", "/order", "POST", "Reject empty request body.", "P0", "API is deployed.", json_header, "", "TD-ORDER-004", "Send POST /order with an empty body.", "400", "Validation error is returned.", "No order id is returned; no DB row is created.", True, "Some frameworks may return 415/422 depending parser."),
        row("FH-API-020", "Order", "/order", "POST", "Reject malformed JSON.", "P0", "API is deployed.", json_header, '{"items":[{"menuItemId":1,"quantity":1}', "TD-ORDER-005", "Send POST /order with malformed JSON.", "400", "Parser error or validation error is returned.", "No 5xx; no DB row is created.", True, "Important negative parser coverage."),
        row("FH-API-021", "Order", "/order", "POST", "Reject missing items field.", "P0", "API is deployed.", json_header, {"customerName": "Missing Items"}, "TD-ORDER-006", "Send POST /order without items.", "400 or 422", "Validation error identifies missing items.", "No order id/orderId returned.", True, "Swagger required fields should drive expected status."),
        row("FH-API-022", "Order", "/order", "POST", "Reject empty items array.", "P0", "API is deployed.", json_header, {"items": [], "customerName": "Empty Items"}, "TD-ORDER-007", "Send POST /order with items: [].", "400 or 422", "Validation error identifies empty order.", "No order id/orderId returned.", True, "Prevents zero-total orders."),
        row("FH-API-023", "Order", "/order", "POST", "Reject missing menuItemId.", "P0", "API is deployed.", json_header, {"items": [{"quantity": 1}], "customerName": "Missing Item"}, "TD-ORDER-008", "Send POST /order with item missing menuItemId.", "400 or 422", "Validation error identifies missing menu item id.", "No order is created.", True, ""),
        row("FH-API-024", "Order", "/order", "POST", "Reject missing quantity.", "P0", "API is deployed.", json_header, {"items": [{"menuItemId": 1}], "customerName": "Missing Quantity"}, "TD-ORDER-009", "Send POST /order with item missing quantity.", "400 or 422", "Validation error identifies missing quantity.", "No order is created.", True, ""),
        row("FH-API-025", "Order", "/order", "POST", "Reject zero quantity.", "P0", "API is deployed.", json_header, {"items": [{"menuItemId": 1, "quantity": 0}], "customerName": "Zero Quantity"}, "TD-ORDER-010", "Send POST /order with quantity 0.", "400 or 422", "Validation error identifies invalid quantity.", "No order is created.", True, ""),
        row("FH-API-026", "Order", "/order", "POST", "Reject negative quantity.", "P0", "API is deployed.", json_header, {"items": [{"menuItemId": 1, "quantity": -1}], "customerName": "Negative Quantity"}, "TD-ORDER-011", "Send POST /order with quantity -1.", "400 or 422", "Validation error identifies invalid quantity.", "No order is created.", True, ""),
        row("FH-API-027", "Order", "/order", "POST", "Reject decimal quantity.", "P1", "API is deployed.", json_header, {"items": [{"menuItemId": 1, "quantity": 1.5}], "customerName": "Decimal Quantity"}, "TD-ORDER-012", "Send POST /order with quantity 1.5.", "400 or 422", "Validation error identifies quantity must be integer.", "No order is created.", True, ""),
        row("FH-API-028", "Order", "/order", "POST", "Reject string quantity.", "P1", "API is deployed.", json_header, {"items": [{"menuItemId": 1, "quantity": "2"}], "customerName": "String Quantity"}, "TD-ORDER-013", "Send POST /order with quantity as a string.", "400 or 422", "Validation error identifies quantity type.", "No order is created.", True, ""),
        row("FH-API-029", "Order", "/order", "POST", "Reject null quantity.", "P1", "API is deployed.", json_header, {"items": [{"menuItemId": 1, "quantity": None}], "customerName": "Null Quantity"}, "TD-ORDER-014", "Send POST /order with null quantity.", "400 or 422", "Validation error identifies quantity is required.", "No order is created.", True, ""),
        row("FH-API-030", "Order", "/order", "POST", "Reject non-existent menu item.", "P0", "API is deployed.", json_header, {"items": [{"menuItemId": 999999, "quantity": 1}], "customerName": "Unknown Item"}, "TD-ORDER-015", "Send POST /order using an id not returned by /menu.", "400, 404, or 422", "Validation error identifies invalid menu item.", "No order is created.", True, ""),
        row("FH-API-031", "Order", "/order", "POST", "Reject invalid menu item id type.", "P1", "API is deployed.", json_header, {"items": [{"menuItemId": "abc", "quantity": 1}], "customerName": "Bad Item Type"}, "TD-ORDER-016", "Send POST /order with string menuItemId when numeric ids are expected.", "400 or 422", "Validation error identifies item id type.", "No order is created.", True, "If Swagger defines string ids, use a non-existent but correctly typed id instead."),
        row("FH-API-032", "Order", "/order", "POST", "Handle duplicate line items consistently.", "P2", "Menu item id 1 exists.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}, {"menuItemId": 1, "quantity": 2}], "customerName": "Duplicate Items"}, "TD-ORDER-017", "Send POST /order with duplicated menuItemId.", "200, 201, or 400", "API either aggregates duplicates or rejects duplicates explicitly.", "No 5xx; total is correct if accepted.", True, "Document chosen product behavior."),
        row("FH-API-033", "Order", "/order", "POST", "Ignore or reject client-supplied total.", "P1", "Menu item id 1 exists.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "Forged Total", "total": 0.01}, "TD-ORDER-018", "Send POST /order with forged total.", "200, 201, or 400", "Server does not trust incorrect client total.", "If accepted, response total is calculated by server, not 0.01.", True, "Security/business rule test."),
        row("FH-API-034", "Order", "/order", "POST", "Reject unexpected nested payload shape.", "P2", "API is deployed.", json_header, {"order": {"items": [{"menuItemId": 1, "quantity": 1}]}, "customerName": "Nested Shape"}, "TD-ORDER-019", "Send POST /order with items nested under order.", "400 or 422", "Validation error identifies missing top-level items.", "No order is created.", True, ""),
        row("FH-API-035", "Order", "/order", "POST", "Reject array as top-level payload.", "P2", "API is deployed.", json_header, [{"menuItemId": 1, "quantity": 1}], "TD-ORDER-020", "Send POST /order with top-level array.", "400 or 422", "Validation error identifies invalid body shape.", "No order is created.", True, ""),
        row("FH-API-036", "Order", "/order", "POST", "Handle very long customer name safely.", "P2", "Menu item id 1 exists.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "A" * 256}, "TD-ORDER-021", "Send POST /order with 256-character customerName.", "200, 201, 400, or 422", "API enforces documented length or stores safely.", "No 5xx; no truncation unless documented.", True, "Use Swagger maxLength if present."),
        row("FH-API-037", "Order", "/order", "POST", "Sanitize customer name script input.", "P1", "Menu item id 1 exists.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "<script>alert(1)</script>"}, "TD-ORDER-022", "Send POST /order with script-like customerName.", "200, 201, 400, or 422", "API does not reflect executable script unsafely.", "No 5xx; response escapes or rejects value.", True, "Security negative test."),
        row("FH-API-038", "Order", "/order", "GET", "Reject unsupported GET on order collection if not documented.", "P2", "API is deployed.", accept_header, "", "TD-ORDER-023", "Send GET /order.", "405 or 404", "API rejects unsupported method or returns documented list only if supported.", "No 5xx.", True, "If Swagger documents GET /order, replace with list-order assertions."),
        row("FH-API-039", "Order", "/order", "POST", "Reject missing Content-Type on JSON body.", "P2", "API is deployed.", "Accept: application/json", valid_single, "TD-ORDER-024", "Send POST /order without Content-Type.", "400, 415, 200, or 201", "API behavior is explicit and does not fail with 5xx.", "If accepted, order is created correctly; if rejected, clear error.", True, "Documents parser tolerance."),
        row("FH-API-040", "Order", "/order", "POST", "Reject unsupported content type.", "P2", "API is deployed.", "Content-Type: text/plain", compact_json(valid_single), "TD-ORDER-025", "Send POST /order as text/plain.", "400 or 415", "API rejects unsupported media type.", "No order is created.", True, ""),
        row("FH-API-041", "Order", "/order", "POST", "Verify order id uniqueness across successful orders.", "P1", "Menu item id 1 exists.", json_header, valid_single, "TD-ORDER-026", "Create two valid orders with same payload.", "200 or 201 each", "Each order receives a distinct id/orderId.", "First id != second id.", True, "Prevents id reuse/collision."),
        row("FH-API-042", "Order", "/order", "POST", "Verify order persistence in database.", "P1", "Database connection is configured.", json_header, valid_single, "TD-ORDER-027", "Create order, capture id/orderId, query orders table by id.", "200 or 201", "Order row exists with matching id and total.", "DB id equals API id; DB total equals API total.", False, "Uses orders table noted in existing project artifacts."),
        row("FH-API-043", "Order", "/order", "POST", "Verify no database row for rejected order.", "P1", "Database connection is configured.", json_header, {"items": [], "customerName": "No Persist"}, "TD-ORDER-028", "Send invalid empty-items order and query recent orders.", "400 or 422", "No new row is persisted.", "Order count unchanged.", True, "Use timestamp/id count strategy based on DB schema."),
        row("FH-API-044", "Order", "/order", "POST", "Handle concurrent order creation.", "P2", "Menu item id 1 exists.", json_header, valid_single, "TD-ORDER-029", "Send 10 concurrent valid POST /order requests.", "200 or 201 each", "All successful responses have unique ids and valid totals.", "No duplicate ids; no 5xx.", True, "Performance/concurrency edge case."),
        row("FH-API-045", "General", "/unknown", "GET", "Unknown endpoint returns not found.", "P2", "API is deployed.", accept_header, "", "TD-GEN-001", "Send GET /unknown.", "404", "Not found response is returned.", "No 5xx; response body does not expose stack trace.", True, "General routing hardening."),
    ]

    data_rows = [
        data("TD-HEALTH-001", "/health", "Standard health check.", accept_header, "", "200", "$.status is string.", ""),
        data("TD-HEALTH-002", "/health", "Unsupported POST method.", json_header, {}, "405 or 404", "No mutation; no 2xx.", ""),
        data("TD-HEALTH-003", "/health?probe=true&x=<script>", "Unexpected query parameters.", accept_header, "", "200", "No unsafe reflection.", ""),
        data("TD-HEALTH-004", "/health", "Invalid auth token should not break public health check.", "Accept: application/json\nAuthorization: Bearer invalid-token", "", "200 or 401", "Behavior matches security design.", ""),
        data("TD-HEALTH-005", "/health", "Repeated health requests.", accept_header, "", "200 for each", "No intermittent errors.", ""),
        data("TD-MENU-001", "/menu", "Standard menu retrieval.", accept_header, "", "200", "Menu items contain id, name, price.", ""),
        data("TD-MENU-002", "/menu", "Unsupported menu mutation.", json_header, {"name": "Injected Item", "price": 0.01}, "405 or 404", "No item created.", ""),
        data("TD-MENU-003", "/menu?category=../../etc/passwd&sort=<script>", "Query/path traversal and script-like input.", accept_header, "", "200 or 400", "No 5xx; no unsafe reflection.", ""),
        data("TD-MENU-004", "/menu", "Wildcard Accept header.", "Accept: */*", "", "200", "JSON-compatible response.", ""),
        data("TD-MENU-005", "/menu", "Unsupported Accept header.", "Accept: text/html", "", "200 or 406", "No malformed response.", ""),
        data("TD-ORDER-001", "/order", "Valid single item order.", json_header, valid_single, "200 or 201", "id/orderId present; total > 0.", ""),
        data("TD-ORDER-002", "/order", "Valid multi-item order.", json_header, valid_multi, "200 or 201", "Total equals menu price calculation.", ""),
        data("TD-ORDER-003", "/order", "Large but plausible quantity.", json_header, {"items": [{"menuItemId": 1, "quantity": 99}], "customerName": "Bulk Test"}, "200, 201, or 400", "No overflow or 5xx.", ""),
        data("TD-ORDER-004", "/order", "Empty body.", json_header, "", "400", "Validation/parser error.", ""),
        data("TD-ORDER-005", "/order", "Malformed JSON.", json_header, '{"items":[{"menuItemId":1,"quantity":1}', "400", "Parser error; no persistence.", ""),
        data("TD-ORDER-006", "/order", "Missing items field.", json_header, {"customerName": "Missing Items"}, "400 or 422", "Missing items error.", ""),
        data("TD-ORDER-007", "/order", "Empty items array.", json_header, {"items": [], "customerName": "Empty Items"}, "400 or 422", "Empty order rejected.", ""),
        data("TD-ORDER-008", "/order", "Missing menuItemId.", json_header, {"items": [{"quantity": 1}], "customerName": "Missing Item"}, "400 or 422", "Missing menuItemId error.", ""),
        data("TD-ORDER-009", "/order", "Missing quantity.", json_header, {"items": [{"menuItemId": 1}], "customerName": "Missing Quantity"}, "400 or 422", "Missing quantity error.", ""),
        data("TD-ORDER-010", "/order", "Zero quantity.", json_header, {"items": [{"menuItemId": 1, "quantity": 0}], "customerName": "Zero Quantity"}, "400 or 422", "Invalid quantity error.", ""),
        data("TD-ORDER-011", "/order", "Negative quantity.", json_header, {"items": [{"menuItemId": 1, "quantity": -1}], "customerName": "Negative Quantity"}, "400 or 422", "Invalid quantity error.", ""),
        data("TD-ORDER-012", "/order", "Decimal quantity.", json_header, {"items": [{"menuItemId": 1, "quantity": 1.5}], "customerName": "Decimal Quantity"}, "400 or 422", "Integer quantity enforced.", ""),
        data("TD-ORDER-013", "/order", "String quantity.", json_header, {"items": [{"menuItemId": 1, "quantity": "2"}], "customerName": "String Quantity"}, "400 or 422", "Numeric quantity enforced.", ""),
        data("TD-ORDER-014", "/order", "Null quantity.", json_header, {"items": [{"menuItemId": 1, "quantity": None}], "customerName": "Null Quantity"}, "400 or 422", "Required quantity enforced.", ""),
        data("TD-ORDER-015", "/order", "Non-existent menu item.", json_header, {"items": [{"menuItemId": 999999, "quantity": 1}], "customerName": "Unknown Item"}, "400, 404, or 422", "Invalid menu item rejected.", ""),
        data("TD-ORDER-016", "/order", "Invalid menuItemId type.", json_header, {"items": [{"menuItemId": "abc", "quantity": 1}], "customerName": "Bad Item Type"}, "400 or 422", "Item id type enforced.", ""),
        data("TD-ORDER-017", "/order", "Duplicate line items.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}, {"menuItemId": 1, "quantity": 2}], "customerName": "Duplicate Items"}, "200, 201, or 400", "Aggregated or rejected consistently.", ""),
        data("TD-ORDER-018", "/order", "Forged client total.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "Forged Total", "total": 0.01}, "200, 201, or 400", "Server-calculated total is authoritative.", ""),
        data("TD-ORDER-019", "/order", "Nested payload shape.", json_header, {"order": {"items": [{"menuItemId": 1, "quantity": 1}]}, "customerName": "Nested Shape"}, "400 or 422", "Top-level items required.", ""),
        data("TD-ORDER-020", "/order", "Top-level array payload.", json_header, [{"menuItemId": 1, "quantity": 1}], "400 or 422", "Object body required.", ""),
        data("TD-ORDER-021", "/order", "Long customer name.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "A" * 256}, "200, 201, 400, or 422", "Length handled explicitly.", ""),
        data("TD-ORDER-022", "/order", "Script-like customer name.", json_header, {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "<script>alert(1)</script>"}, "200, 201, 400, or 422", "No unsafe reflection.", ""),
        data("TD-ORDER-023", "/order", "Unsupported GET order collection.", accept_header, "", "405 or 404", "No 5xx.", ""),
        data("TD-ORDER-024", "/order", "Missing Content-Type.", "Accept: application/json", valid_single, "400, 415, 200, or 201", "Explicit parser behavior.", ""),
        data("TD-ORDER-025", "/order", "Unsupported text/plain content type.", "Content-Type: text/plain", compact_json(valid_single), "400 or 415", "Unsupported media type rejected.", ""),
        data("TD-ORDER-026", "/order", "Two valid orders for uniqueness check.", json_header, valid_single, "200 or 201 each", "Distinct ids.", ""),
        data("TD-ORDER-027", "/order", "Valid order with DB persistence validation.", json_header, valid_single, "200 or 201", "DB row matches API response.", ""),
        data("TD-ORDER-028", "/order", "Invalid order for no-persistence validation.", json_header, {"items": [], "customerName": "No Persist"}, "400 or 422", "No DB row created.", ""),
        data("TD-ORDER-029", "/order", "Concurrent valid orders.", json_header, valid_single, "200 or 201 each", "Unique ids; no 5xx.", ""),
        data("TD-GEN-001", "/unknown", "Unknown route.", accept_header, "", "404", "No stack trace.", ""),
    ]
    return cases, data_rows


def build_import_rows():
    json_header = "Content-Type: application/json"
    accept_header = "Accept: application/json"
    valid_payload = {"items": [{"menuItemId": 1, "quantity": 1}], "customerName": "API Test User"}
    invalid_empty = {"items": [], "customerName": "Empty Items"}
    import_rows = [
        [SUITE_NAME, "Health API", "GET /health returns status", hit("/health", "GET", accept_header), "", "", compact_json([validation("$.status", "String")]), "", "", "", "", "", "", "", "", "", "true", "Sequential", "Ready"],
        [SUITE_NAME, "Menu API", "GET /menu returns item schema", hit("/menu", "GET", accept_header), "", "", compact_json([validation("$[0].id", "Number"), validation("$[0].name", "String"), validation("$[0].price", "Number")]), "", "", "", "", "", "", "", "", "", "true", "Sequential", "Ready"],
        [SUITE_NAME, "Order API", "POST /order creates valid order", hit("/order", "POST", json_header), compact_json(valid_payload), "orderId=$.id;orderIdAlt=$.orderId", compact_json([validation("$.total", "Number")]), "", "", "", "", "", "", "", "", "", "true", "Sequential", "Ready"],
        [SUITE_NAME, "Order API Negative", "POST /order rejects empty items", hit("/order", "POST", json_header), compact_json(invalid_empty), "", "", "", "", "", "", "", "", "", "", "", "true", "Sequential", "Ready"],
        [SUITE_NAME, "Order API Negative", "POST /order rejects malformed JSON", hit("/order", "POST", json_header), '{"items":[{"menuItemId":1,"quantity":1}', "", "", "", "", "", "", "", "", "", "", "", "true", "Sequential", "Ready"],
        [SUITE_NAME, "Order DB Validation", "Verify created order persisted", hit("/order", "POST", json_header), compact_json(valid_payload), "orderId=$.id", "", "", "", '{"sqlQuery":"SELECT id, total FROM orders WHERE id = \'${orderId}\'"}', '{"databaseType":"PostgreSQL","password":"${dbPassword}","driverClass":"org.postgresql.Driver","jdbcUrl":"${dbJdbcUrl}","username":"${dbUser}"}', "SELECT id, total FROM orders WHERE id = '${orderId}'", compact_json([{"apiField": "$.id", "description": "Order ID matches database", "operator": "=", "dbColumn": "id"}, {"apiField": "$.total", "description": "Order total matches database", "operator": "=", "dbColumn": "total"}]), compact_json([{"nullValidation": "Not Null", "typeValidation": "String", "dbColumnName": "id", "expectedValueOrVariable": ""}, {"nullValidation": "Not Null", "typeValidation": "Number", "dbColumnName": "total", "expectedValueOrVariable": ""}]), "", "", "true", "Sequential", "Ready"],
    ]
    return import_rows


def col_name(index):
    name = ""
    while index:
        index, rem = divmod(index - 1, 26)
        name = chr(65 + rem) + name
    return name


def sheet_xml(rows):
    row_xml = []
    for r_idx, row_values in enumerate(rows, start=1):
        cells = []
        for c_idx, value in enumerate(row_values, start=1):
            ref = f"{col_name(c_idx)}{r_idx}"
            text = "" if value is None else str(value)
            text = escape(text, {"\n": "&#10;"})
            cells.append(f'<c r="{ref}" t="inlineStr"><is><t>{text}</t></is></c>')
        row_xml.append(f'<row r="{r_idx}">{"".join(cells)}</row>')
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" '
        'xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">'
        '<sheetViews><sheetView workbookViewId="0"/></sheetViews>'
        '<sheetFormatPr defaultRowHeight="15"/>'
        '<sheetData>'
        + "".join(row_xml)
        + '</sheetData></worksheet>'
    )


def workbook_xml(sheet_names):
    sheets = []
    for idx, name in enumerate(sheet_names, start=1):
        sheets.append(f'<sheet name="{escape(name)}" sheetId="{idx}" r:id="rId{idx}"/>')
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" '
        'xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">'
        '<sheets>'
        + "".join(sheets)
        + '</sheets></workbook>'
    )


def workbook_rels(sheet_count):
    rels = []
    for idx in range(1, sheet_count + 1):
        rels.append(
            f'<Relationship Id="rId{idx}" '
            'Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" '
            f'Target="worksheets/sheet{idx}.xml"/>'
        )
    rels.append(
        f'<Relationship Id="rId{sheet_count + 1}" '
        'Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" '
        'Target="styles.xml"/>'
    )
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'
        + "".join(rels)
        + '</Relationships>'
    )


def content_types(sheet_count):
    overrides = [
        '<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>',
        '<Default Extension="xml" ContentType="application/xml"/>',
        '<Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>',
        '<Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>',
        '<Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>',
        '<Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>',
    ]
    for idx in range(1, sheet_count + 1):
        overrides.append(
            f'<Override PartName="/xl/worksheets/sheet{idx}.xml" '
            'ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>'
        )
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">'
        + "".join(overrides)
        + '</Types>'
    )


def root_rels():
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'
        '<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>'
        '<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>'
        '<Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>'
        '</Relationships>'
    )


def styles_xml():
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">'
        '<fonts count="1"><font><sz val="11"/><name val="Calibri"/></font></fonts>'
        '<fills count="1"><fill><patternFill patternType="none"/></fill></fills>'
        '<borders count="1"><border><left/><right/><top/><bottom/><diagonal/></border></borders>'
        '<cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>'
        '<cellXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/></cellXfs>'
        '<cellStyles count="1"><cellStyle name="Normal" xfId="0" builtinId="0"/></cellStyles>'
        '</styleSheet>'
    )


def props():
    now = datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")
    core = (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" '
        'xmlns:dc="http://purl.org/dc/elements/1.1/" '
        'xmlns:dcterms="http://purl.org/dc/terms/" '
        'xmlns:dcmitype="http://purl.org/dc/dcmitype/" '
        'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">'
        '<dc:creator>Codex</dc:creator>'
        '<cp:lastModifiedBy>Codex</cp:lastModifiedBy>'
        f'<dcterms:created xsi:type="dcterms:W3CDTF">{now}</dcterms:created>'
        f'<dcterms:modified xsi:type="dcterms:W3CDTF">{now}</dcterms:modified>'
        '</cp:coreProperties>'
    )
    app = (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" '
        'xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">'
        '<Application>Codex</Application></Properties>'
    )
    return core, app


def write_xlsx(path, sheets):
    path.parent.mkdir(parents=True, exist_ok=True)
    names = list(sheets)
    core, app = props()
    with zipfile.ZipFile(path, "w", compression=zipfile.ZIP_DEFLATED) as zf:
        zf.writestr("[Content_Types].xml", content_types(len(names)))
        zf.writestr("_rels/.rels", root_rels())
        zf.writestr("xl/workbook.xml", workbook_xml(names))
        zf.writestr("xl/_rels/workbook.xml.rels", workbook_rels(len(names)))
        zf.writestr("xl/styles.xml", styles_xml())
        zf.writestr("docProps/core.xml", core)
        zf.writestr("docProps/app.xml", app)
        for idx, name in enumerate(names, start=1):
            zf.writestr(f"xl/worksheets/sheet{idx}.xml", sheet_xml(sheets[name]))


def main():
    cases, test_data = build_cases()
    import_rows = build_import_rows()
    sheets = {
        "Test Cases": [HEADERS] + cases,
        "Test Data": [DATA_HEADERS] + test_data,
        "TestWeave Import": [IMPORT_HEADERS] + import_rows,
    }
    out_dir = Path("/workspace/C/Users/sai93/Documents/TestWeave/TestCases")
    out_path = out_dir / "FoodHub_Swagger_API_TestCases_TestData.xlsx"
    write_xlsx(out_path, sheets)
    mirror_dir = Path("/workspace/TestCases")
    mirror_path = mirror_dir / out_path.name
    write_xlsx(mirror_path, sheets)
    print(out_path)
    print(mirror_path)
    print(f"test_cases={len(cases)} test_data={len(test_data)} import_rows={len(import_rows)}")


if __name__ == "__main__":
    main()
