package ui;

import compare.JsonComparator;
import model.ApiRequest;
import model.ApiResponse;
import model.DbConnectionConfig;
import model.DbValidationReport;
import model.DbValidationResult;
import model.DbValidationRule;
import model.PerformanceTestResult;
import model.ResponseFieldCandidate;
import model.WebTestCase;
import model.WebTestExecutionResult;
import model.WebTestRunReport;
import model.WebTestStep;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import service.ApiService;
import service.DbValidationService;
import service.PerformanceTestService;
import service.PlaywrightRecorderController;
import service.ResponseVariableService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ApiValidatorUI extends JFrame {

    private static final String APP_NAME = "TestWeave";
    private static final String APP_LOGO_RESOURCE = "/testweave-logo.png";
    private static final Color PRIMARY = new Color(30, 94, 214);
    private static final Color BORDER = new Color(210, 220, 235);
    private static final Color PANEL_BG = Color.WHITE;
    private static final Color APP_BG = new Color(245, 247, 251);
    private static final Color SUCCESS = new Color(18, 134, 74);
    private static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font MONO_FONT = new Font("Consolas", Font.PLAIN, 15);
    private static final String APP_VERSION = resolveAppVersion();
    private static final List<String> RUNTIME_VARIABLES = List.of("randomString", "randomInt", "randomDate");

    private JTextField endpointField;
    private JPasswordField tokenField;
    private JTextField filePathField;
    private JComboBox<String> methodDropdown;
    private JComboBox<String> compareModeDropdown;
    private JComboBox<String> authTypeDropdown;
    private JComboBox<String> requestFormatDropdown;
    private JTextArea headersArea;
    private JTextArea bodyArea;
    private JTextArea prettyResponseArea;
    private JTextArea rawResponseArea;
    private JTextArea responseHeadersArea;
    private JTextArea responseCookiesArea;
    private DefaultTableModel responseFieldsTableModel;
    private JTable responseFieldsTable;
    private JLabel responseFieldsStatusLabel;
    private DefaultTableModel fieldValidationsTableModel;
    private JTable fieldValidationsTable;
    private JLabel fieldValidationsStatusLabel;
    private DefaultTableModel tableModel;
    private JLabel statusValueLabel;
    private JLabel timeValueLabel;
    private JLabel sizeValueLabel;
    private char defaultEchoChar;
    private JButton sendRequestButton;
    private JButton clearButton;
    private JLabel requestBodyTitleLabel;
    private String lastExpectedJson;
    private String lastActualJson;
    private boolean lastStrictCompare;
    private JLabel performanceSourceLabel;
    private JLabel performanceConfigLabel;
    private JLabel performanceSamplesLabel;
    private JLabel performanceErrorsLabel;
    private JLabel performanceThroughputLabel;
    private JLabel performanceDurationLabel;
    private JLabel performanceReportLabel;
    private JTextArea performanceLogArea;
    private JButton runLoadTestButton;
    private JButton openReportButton;
    private PerformanceChartPanel performanceChartPanel;
    private Path lastPerformanceReportPath;
    private JSpinner performanceThreadsSpinner;
    private JSpinner performanceIterationsSpinner;
    private JTextArea performanceBodyArea;
    private JLabel performanceBodyTitleLabel;
    private JComboBox<String> dbTypeDropdown;
    private JTextField jdbcUrlField;
    private JTextField dbUsernameField;
    private JPasswordField dbPasswordField;
    private JTextField driverClassField;
    private JTextArea dbQueryArea;
    private DefaultTableModel dbRulesTableModel;
    private DefaultTableModel dbResultsTableModel;
    private DefaultTableModel dbQueryResultsTableModel;
    private DefaultTableModel dbColumnValidationsTableModel;
    private JTable dbRulesTable;
    private JTable dbQueryResultsTable;
    private JTable dbColumnValidationsTable;
    private JLabel dbColumnValidationsStatusLabel;
    private final List<String> dbColumnOptions = new ArrayList<>();
    private int dbColumnOptionRowCount;
    private JLabel dbConnectionStatusLabel;
    private JLabel dbSummaryLabel;
    private JLabel dbPassedLabel;
    private JLabel dbFailedLabel;
    private JButton testDbConnectionButton;
    private JButton runDbValidationButton;
    private JButton runDbQueryButton;
    private JButton saveSelectedDbCellButton;
    private char dbDefaultEchoChar;
    private String lastApiResponseBody;
    private JTextField webTestNameField;
    private JTextField webStartUrlField;
    private JTextField webCdpEndpointField;
    private DefaultTableModel webStepsTableModel;
    private DefaultTableModel webResultsTableModel;
    private JTable webStepsTable;
    private JTable webResultsTable;
    private JLabel webRecorderStatusLabel;
    private JLabel webBrowserUrlLabel;
    private JLabel webSelectorHintLabel;
    private JLabel webRunSummaryLabel;
    private JLabel webRunPassedLabel;
    private JLabel webRunFailedLabel;
    private JLabel webScreenshotLabel;
    private JCheckBox webHeadlessCheckbox;
    private JCheckBox webSlowMoCheckbox;
    private JButton webRecordButton;
    private JButton webAttachButton;
    private JButton webLaunchDebugChromeButton;
    private JButton webStopButton;
    private JButton webStopNoBrowserCloseButton;
    private JButton webRunButton;
    private JButton webStopRunButton;
    private JButton webRetestButton;
    private JTextArea webTipsArea;
    private DefaultTableModel savedVariablesTableModel;
    private JTable savedVariablesTable;
    private JComboBox<String> apiUrlVariableDropdown;
    private JComboBox<String> apiVariableDropdown;
    private JComboBox<String> dbVariableDropdown;
    private JComboBox<String> webVariableDropdown;
    private JTextField testSuiteNameField;
    private JTextField testCaseNameField;
    private JTextField testSuiteWorkbookPathField;
    private JButton createTestSuiteWorkbookButton;
    private JButton importTestSuiteWorkbookButton;
    private DefaultTableModel testSuiteRunnerStepsTableModel;
    private JTable testSuiteRunnerStepsTable;
    private JLabel testSuiteRunnerStatusLabel;
    private JButton runTestSuiteButton;
    private JButton openTestSuiteReportButton;
    private JToggleButton parallelExecutionSwitch;
    private JTextField threadCountField;
    private Path lastTestSuiteReportPath;
    private JTextField fieldValidationTestSuiteField;
    private JTextField fieldValidationTestCaseField;
    private JTextField fieldValidationTestStepField;
    private JTextField jsonCompareTestSuiteField;
    private JTextField jsonCompareTestCaseField;
    private JTextField jsonCompareTestStepField;
    private JTextField dbValidationTestSuiteField;
    private JTextField dbValidationTestCaseField;
    private JTextField dbValidationTestStepField;
    private JTextField webTestingTestSuiteField;
    private JTextField webTestingTestCaseField;
    private JTextField webTestingTestStepField;
    private JTextField performanceTestSuiteField;
    private JTextField performanceTestCaseField;
    private JTextField performanceTestStepField;
    private Path dbConnectionFilePath;

    private final ApiService apiService = new ApiService();
    private final JsonComparator comparator = new JsonComparator();
    private final PerformanceTestService performanceTestService = new PerformanceTestService();
    private final DbValidationService dbValidationService = new DbValidationService();
    private final PlaywrightRecorderController playwrightRecorderController = new PlaywrightRecorderController();
    private final ResponseVariableService responseVariableService = new ResponseVariableService();
    private final Map<String, String> savedVariables = new ConcurrentHashMap<>();
    private final Map<String, String> savedVariablePaths = new ConcurrentHashMap<>();
    private final Map<String, String> savedVariableTypes = new ConcurrentHashMap<>();
    private final List<JComboBox<String>> variableDropdowns = new ArrayList<>();

    public ApiValidatorUI() {
        setTitle(APP_NAME + " - " + APP_VERSION);
        setApplicationIcon();
        setSize(1540, 1040);
        setMinimumSize(new Dimension(1280, 820));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(APP_BG);
        UIManager.put("TabbedPane.selected", Color.WHITE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UI_FONT);
        tabs.setBackground(APP_BG);
        tabs.addTab("API Tester", createApiPanel());
        tabs.addTab("API Validation", createApiValidationPanel());
        tabs.addTab("Performance Test", createPerformancePanel());
        tabs.addTab("DB Validator", createDbValidatorPanel());
        tabs.addTab("Web Testing", createWebTestingPanel());
        tabs.addTab("Test Suite Runner", createTestSuiteRunnerPanel());
        tabs.addTab("Variables", createVariablesPanel());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(APP_BG);
        container.add(createTopStatusBar(), BorderLayout.NORTH);
        container.add(tabs, BorderLayout.CENTER);

        add(container);
        setVisible(true);
    }

    private void setApplicationIcon() {
        URL logoUrl = ApiValidatorUI.class.getResource(APP_LOGO_RESOURCE);
        if (logoUrl != null) {
            setIconImage(new ImageIcon(logoUrl).getImage());
        }
    }

    private JPanel createTopStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(APP_BG);
        panel.setBorder(new EmptyBorder(6, 14, 4, 14));

        JLabel versionLabel = new JLabel("Build: " + APP_VERSION);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        versionLabel.setForeground(new Color(95, 103, 120));

        JLabel runtimeLabel = new JLabel("Runtime: bundled dependencies");
        runtimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        runtimeLabel.setForeground(new Color(95, 103, 120));

        panel.add(versionLabel, BorderLayout.WEST);
        panel.add(runtimeLabel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createApiPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        root.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 16, 18, 16)
        ));

        JPanel content = new JPanel();
        content.setBackground(PANEL_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel requestSection = createRequestSection();
        requestSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(requestSection);
        content.add(Box.createVerticalStrut(18));

        JPanel responseSection = createResponseSection();
        responseSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(responseSection);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(scrollPane, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel createRequestSection() {
        JPanel section = new JPanel();
        section.setBackground(PANEL_BG);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));

        JLabel title = createSectionTitle("Request Details");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(title);
        section.add(Box.createVerticalStrut(10));

        JPanel requestRow = new JPanel(new GridBagLayout());
        requestRow.setBackground(PANEL_BG);
        requestRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        methodDropdown = new JComboBox<>(new String[]{"GET", "POST", "PUT", "DELETE", "PATCH"});
        endpointField = new JTextField("https://jsonplaceholder.typicode.com/posts/1");
        apiUrlVariableDropdown = createVariableDropdown();
        JButton insertUrlVariableBtn = createSecondaryButton("Insert");
        insertUrlVariableBtn.addActionListener(e -> insertSelectedVariable(endpointField, apiUrlVariableDropdown));
        authTypeDropdown = new JComboBox<>(new String[]{"None", "Bearer Token"});
        tokenField = new JPasswordField("");
        defaultEchoChar = tokenField.getEchoChar();
        tokenField.setEnabled(false);
        methodDropdown.addActionListener(e -> {
            updateRequestBodyState();
            updatePerformanceBodyState();
        });

        JButton toggleTokenBtn = createSecondaryButton("Show");
        toggleTokenBtn.addActionListener(e -> toggleTokenVisibility(toggleTokenBtn));
        authTypeDropdown.addActionListener(e -> updateAuthControls(toggleTokenBtn));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.16;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 16);
        requestRow.add(createCompactLabeledPanel("Method", wrapComponent(methodDropdown)), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.84;
        gbc.insets = new Insets(0, 0, 12, 0);
        requestRow.add(createCompactLabeledPanel("Endpoint",
                wrapWithTrailingButton(wrapWithTrailingButton(endpointField, apiUrlVariableDropdown), insertUrlVariableBtn)), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.32;
        gbc.insets = new Insets(0, 0, 0, 16);
        requestRow.add(createCompactLabeledPanel("Auth Type", wrapComponent(authTypeDropdown)), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.68;
        gbc.insets = new Insets(0, 0, 0, 0);
        requestRow.add(createCompactLabeledPanel("Token", wrapWithTrailingButton(tokenField, toggleTokenBtn)), gbc);

        requestRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        section.add(requestRow);
        section.add(Box.createVerticalStrut(18));

        headersArea = createEditorArea(
                "Accept: application/json\nContent-Type: application/json\nUser-Agent: API-Validator-Tool/1.0"
        );
        headersArea.setRows(5);
        bodyArea = createEditorArea("");
        bodyArea.setRows(10);

        JPanel bodyActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bodyActions.setBackground(PANEL_BG);
        requestFormatDropdown = new JComboBox<>(new String[]{"JSON"});
        requestFormatDropdown.setFont(UI_FONT);
        JButton beautifyBtn = createSecondaryButton("Beautify");
        beautifyBtn.addActionListener(e -> beautifyRequestBody());
        apiVariableDropdown = createVariableDropdown();
        addRuntimeVariableOptions(apiVariableDropdown);
        JButton insertApiVariableBtn = createSecondaryButton("Insert Variable");
        insertApiVariableBtn.addActionListener(e -> insertSelectedVariable(bodyArea, apiVariableDropdown));
        bodyActions.add(new JLabel("Format:"));
        bodyActions.add(requestFormatDropdown);
        bodyActions.add(apiVariableDropdown);
        bodyActions.add(insertApiVariableBtn);
        bodyActions.add(beautifyBtn);

        JPanel editorsColumn = new JPanel();
        editorsColumn.setBackground(PANEL_BG);
        editorsColumn.setLayout(new BoxLayout(editorsColumn, BoxLayout.Y_AXIS));
        editorsColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane headersScrollPane = createLineNumberScrollPane(headersArea);
        headersScrollPane.setPreferredSize(new Dimension(1100, 140));
        headersScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        JPanel headersCard = createEditorCard("Headers (key : value)", headersScrollPane, null);
        headersCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        editorsColumn.add(headersCard);
        editorsColumn.add(Box.createVerticalStrut(16));

        JScrollPane bodyScrollPane = createLineNumberScrollPane(bodyArea);
        bodyScrollPane.setPreferredSize(new Dimension(1100, 220));
        bodyScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        JPanel bodyCard = createEditorCardWithTrackedTitle("Request Body (Optional)", bodyScrollPane, bodyActions);
        bodyCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        editorsColumn.add(bodyCard);

        section.add(editorsColumn);
        section.add(Box.createVerticalStrut(16));

        JPanel actionsRow = new JPanel(new BorderLayout());
        actionsRow.setBackground(PANEL_BG);
        actionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        leftActions.setBackground(PANEL_BG);
        sendRequestButton = createPrimaryButton("Send Request");
        clearButton = createSecondaryButton("Clear");
        sendRequestButton.addActionListener(e -> sendRequest());
        clearButton.addActionListener(e -> clearApiForm());
        leftActions.add(sendRequestButton);
        leftActions.add(clearButton);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightActions.setBackground(PANEL_BG);
        JButton saveResponseBtn = createSecondaryButton("Save Response");
        saveResponseBtn.addActionListener(e -> saveResponse());
        JButton saveBtn = createSecondaryButton("Save Request");
        saveBtn.addActionListener(e -> saveRequest());
        rightActions.add(saveResponseBtn);
        rightActions.add(Box.createHorizontalStrut(10));
        rightActions.add(saveBtn);

        actionsRow.add(leftActions, BorderLayout.WEST);
        actionsRow.add(rightActions, BorderLayout.EAST);
        actionsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        section.add(actionsRow);
        updateRequestBodyState();

        return section;
    }

    private JPanel createResponseSection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setBackground(PANEL_BG);
        section.setBorder(new EmptyBorder(10, 0, 0, 0));
        section.setPreferredSize(new Dimension(0, 420));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 460));

        JLabel title = createSectionTitle("Response");
        section.add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER));

        JTabbedPane responseTabs = new JTabbedPane();
        responseTabs.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        responseTabs.setBackground(PANEL_BG);

        prettyResponseArea = createResponseArea();
        rawResponseArea = createResponseArea();
        responseHeadersArea = createResponseArea();
        responseCookiesArea = createResponseArea();

        responseTabs.addTab("Pretty", createResponseTabPanel(createPrettyPane()));
        responseTabs.addTab("Raw", createResponseTabPanel(new JScrollPane(rawResponseArea)));
        responseTabs.addTab("Headers", createResponseTabPanel(new JScrollPane(responseHeadersArea)));
        responseTabs.addTab("Cookies", createResponseTabPanel(new JScrollPane(responseCookiesArea)));
        responseTabs.addTab("Capture Variables", createResponseVariableCapturePanel());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PANEL_BG);
        topBar.setBorder(new EmptyBorder(4, 8, 0, 8));

        JPanel metrics = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 0));
        metrics.setBackground(PANEL_BG);
        statusValueLabel = createMetricValue("Not sent", new Color(100, 110, 125));
        timeValueLabel = createMetricValue("--", PRIMARY);
        sizeValueLabel = createMetricValue("--", SUCCESS);
        metrics.add(createMetricPanel("Status:", statusValueLabel));
        metrics.add(createMetricPanel("Time:", timeValueLabel));
        metrics.add(createMetricPanel("Size:", sizeValueLabel));

        JButton copyBtn = createSecondaryButton("Copy");
        copyBtn.addActionListener(e -> copyResponse());
        metrics.add(copyBtn);

        topBar.add(metrics, BorderLayout.EAST);

        card.add(topBar, BorderLayout.NORTH);
        card.add(responseTabs, BorderLayout.CENTER);
        section.add(card, BorderLayout.CENTER);
        return section;
    }

    private JPanel createApiValidationPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        root.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 16, 18, 16)
        ));

        JTabbedPane apiValidationTabs = new JTabbedPane();
        apiValidationTabs.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        apiValidationTabs.setBackground(PANEL_BG);
        apiValidationTabs.addTab("Field Validation", createFieldValidationsPanel());
        apiValidationTabs.addTab("JSON Compare", createComparePanel());

        card.add(apiValidationTabs, BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel createComparePanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PANEL_BG);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(PANEL_BG);

        JPanel controls = new JPanel(new GridBagLayout());
        controls.setBackground(PANEL_BG);

        filePathField = new JTextField();
        compareModeDropdown = new JComboBox<>(new String[]{"LENIENT", "STRICT"});
        JButton browse = createSecondaryButton("Browse");
        JButton compareBtn = createPrimaryButton("Compare");
        JButton showMatchedBtn = createSecondaryButton("Show Matched");
        jsonCompareTestSuiteField = new JTextField();
        jsonCompareTestCaseField = new JTextField();
        jsonCompareTestStepField = new JTextField();

        browse.addActionListener(e -> chooseFile());
        compareBtn.addActionListener(e -> runCompare());
        showMatchedBtn.addActionListener(e -> runCompare(true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        controls.add(createLabeledPanel("Expected JSON File", filePathField), gbc);

        gbc.weightx = 0;
        gbc.gridx = 1;
        gbc.insets = new Insets(22, 0, 12, 12);
        controls.add(browse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 12);
        controls.add(createLabeledPanel("Compare Mode", compareModeDropdown), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(22, 0, 0, 12);
        controls.add(compareBtn, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(22, 0, 0, 0);
        controls.add(showMatchedBtn, gbc);

        JPanel northPanel = new JPanel(new BorderLayout(0, 12));
        northPanel.setBackground(PANEL_BG);
        northPanel.add(createTestRunnerContextPanel(jsonCompareTestSuiteField, jsonCompareTestCaseField,
                jsonCompareTestStepField, this::addJsonCompareToTestRunner), BorderLayout.NORTH);
        northPanel.add(controls, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(
                new String[]{"Type", "Field", "Expected", "Actual"}, 0
        );
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);

        card.add(northPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel createResponseVariableCapturePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(PANEL_BG);
        responseFieldsStatusLabel = createMetricValue("Send a request to list capturable response fields.", new Color(95, 103, 120));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(PANEL_BG);
        JButton selectAllBtn = createSecondaryButton("Select All");
        JButton selectTopLevelBtn = createSecondaryButton("Select Top Level");
        JButton clearBtn = createSecondaryButton("Clear");
        JButton saveBtn = createPrimaryButton("Save Selected Variables");
        selectAllBtn.addActionListener(e -> setAllResponseFieldSelections(true));
        selectTopLevelBtn.addActionListener(e -> selectTopLevelResponseFields());
        clearBtn.addActionListener(e -> setAllResponseFieldSelections(false));
        saveBtn.addActionListener(e -> saveSelectedResponseVariables());
        actions.add(selectAllBtn);
        actions.add(selectTopLevelBtn);
        actions.add(clearBtn);
        actions.add(saveBtn);
        toolbar.add(responseFieldsStatusLabel, BorderLayout.WEST);
        toolbar.add(actions, BorderLayout.EAST);

        responseFieldsTableModel = new DefaultTableModel(
                new String[]{"Save", "JSON Path", "Preview Value", "Variable Name", "Type", "Value"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 3;
            }
        };
        responseFieldsTable = new JTable(responseFieldsTableModel);
        responseFieldsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        responseFieldsTable.setRowHeight(26);
        responseFieldsTable.getColumnModel().getColumn(0).setMaxWidth(70);
        responseFieldsTable.getColumnModel().getColumn(4).setMaxWidth(100);
        responseFieldsTable.getColumnModel().getColumn(5).setMinWidth(0);
        responseFieldsTable.getColumnModel().getColumn(5).setMaxWidth(0);
        responseFieldsTable.getColumnModel().getColumn(5).setPreferredWidth(0);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(responseFieldsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFieldValidationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        fieldValidationTestSuiteField = new JTextField();
        fieldValidationTestCaseField = new JTextField();
        fieldValidationTestStepField = new JTextField();

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(PANEL_BG);
        fieldValidationsStatusLabel = createMetricValue("Send a request to list response fields for validation.", new Color(95, 103, 120));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(PANEL_BG);
        JButton validateBtn = createPrimaryButton("Validate Fields");
        JButton resetBtn = createSecondaryButton("Reset Defaults");
        validateBtn.addActionListener(e -> runFieldValidations());
        resetBtn.addActionListener(e -> resetFieldValidationDefaults());
        actions.add(resetBtn);
        actions.add(validateBtn);
        toolbar.add(fieldValidationsStatusLabel, BorderLayout.WEST);
        toolbar.add(actions, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout(0, 12));
        northPanel.setBackground(PANEL_BG);
        northPanel.add(createTestRunnerContextPanel(fieldValidationTestSuiteField, fieldValidationTestCaseField,
                fieldValidationTestStepField, this::addFieldValidationsToTestRunner), BorderLayout.NORTH);
        northPanel.add(toolbar, BorderLayout.CENTER);

        fieldValidationsTableModel = new DefaultTableModel(
                new String[]{"Add", "JSON Path", "Preview Value", "Null Validation", "Type Validation", "Expected Value / Variable", "Result", "Actual Value", "Actual Type"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 3 || column == 4 || column == 5;
            }
        };
        fieldValidationsTable = new JTable(fieldValidationsTableModel);
        fieldValidationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fieldValidationsTable.setRowHeight(26);
        fieldValidationsTable.putClientProperty("terminateEditOnFocusLost", true);
        fieldValidationsTable.getColumnModel().getColumn(0).setMaxWidth(70);
        fieldValidationsTable.getColumnModel().getColumn(3).setCellEditor(
                new DefaultCellEditor(new JComboBox<>(new String[]{"Not Null", "Null", "Skip"}))
        );
        fieldValidationsTable.getColumnModel().getColumn(4).setCellEditor(
                new DefaultCellEditor(new JComboBox<>(jsonTypeValidationOptions()))
        );
        fieldValidationsTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        fieldValidationsTable.getColumnModel().getColumn(7).setMinWidth(0);
        fieldValidationsTable.getColumnModel().getColumn(7).setMaxWidth(0);
        fieldValidationsTable.getColumnModel().getColumn(7).setPreferredWidth(0);
        fieldValidationsTable.getColumnModel().getColumn(8).setMinWidth(0);
        fieldValidationsTable.getColumnModel().getColumn(8).setMaxWidth(0);
        fieldValidationsTable.getColumnModel().getColumn(8).setPreferredWidth(0);
        applyFieldValidationExpectedValueEditor();

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(fieldValidationsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createVariablesPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        root.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 16, 18, 16)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.add(createSectionTitle("Saved Variables"), BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(PANEL_BG);
        JButton createVariableBtn = createSecondaryButton("Create Variable");
        JButton saveVariablesBtn = createSecondaryButton("Save Variables");
        JButton importVariablesBtn = createSecondaryButton("Import Variables");
        JButton removeBtn = createSecondaryButton("Remove Selected");
        createVariableBtn.addActionListener(e -> createSavedVariable());
        saveVariablesBtn.addActionListener(e -> saveVariablesToFile());
        importVariablesBtn.addActionListener(e -> importVariablesFromFile());
        removeBtn.addActionListener(e -> removeSelectedSavedVariable());
        actions.add(createVariableBtn);
        actions.add(saveVariablesBtn);
        actions.add(importVariablesBtn);
        actions.add(removeBtn);
        header.add(actions, BorderLayout.EAST);

        savedVariablesTableModel = new DefaultTableModel(
                new String[]{"Variable", "Value", "JSON Path", "Type"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        savedVariablesTable = new JTable(savedVariablesTableModel);
        savedVariablesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        savedVariablesTable.setRowHeight(28);

        card.add(header, BorderLayout.NORTH);
        card.add(new JScrollPane(savedVariablesTable), BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel createTestSuiteRunnerPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        root.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 16, 18, 16)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.add(createSectionTitle("Test Suite Runner"), BorderLayout.WEST);

        JPanel controls = createTestSuiteWorkbookControls();

        JPanel canvasBoard = createTestCaseCanvasBoard();

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(PANEL_BG);
        content.add(controls, BorderLayout.NORTH);
        content.add(canvasBoard, BorderLayout.CENTER);

        card.add(header, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel createTestSuiteWorkbookControls() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setBackground(PANEL_BG);
        wrapper.setBorder(new EmptyBorder(0, 0, 4, 0));

        JPanel inputRow = new JPanel(new GridBagLayout());
        inputRow.setBackground(PANEL_BG);

        testSuiteNameField = new JTextField();
        testCaseNameField = new JTextField();
        testSuiteWorkbookPathField = new JTextField();
        testSuiteWorkbookPathField.setEditable(false);
        testSuiteWorkbookPathField.setBackground(new Color(249, 251, 255));
        createTestSuiteWorkbookButton = createPrimaryButton("Create");
        createTestSuiteWorkbookButton.addActionListener(e -> createTestSuiteWorkbook());
        importTestSuiteWorkbookButton = createSecondaryButton("Import");
        importTestSuiteWorkbookButton.addActionListener(e -> importTestSuiteWorkbook());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.45;
        gbc.insets = new Insets(0, 0, 0, 12);
        inputRow.add(createCompactLabeledPanel("Test Suite", testSuiteNameField), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        inputRow.add(createCompactLabeledPanel("Test Case", testCaseNameField), gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(22, 0, 0, 8);
        inputRow.add(createTestSuiteWorkbookButton, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(22, 0, 0, 0);
        inputRow.add(importTestSuiteWorkbookButton, gbc);

        wrapper.add(inputRow, BorderLayout.NORTH);
        wrapper.add(createCompactLabeledPanel("Created Workbook Path", testSuiteWorkbookPathField), BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createTestRunnerContextPanel(JTextField testSuiteField, JTextField testCaseField,
                                                JTextField testStepField, Runnable addAction) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(PANEL_BG);

        JButton addToTestRunnerButton = createSecondaryButton("Add to Test Runner");
        if (addAction != null) {
            addToTestRunnerButton.addActionListener(e -> addAction.run());
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.45;
        gbc.insets = new Insets(0, 0, 0, 12);
        row.add(createCompactLabeledPanel("Test Suite", testSuiteField), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.32;
        row.add(createCompactLabeledPanel("Test Case", testCaseField), gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.32;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 12);
        row.add(createCompactLabeledPanel("Test Step", testStepField), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(22, 0, 0, 0);
        row.add(addToTestRunnerButton, gbc);

        return row;
    }

    private JPanel createTestCaseCanvasBoard() {
        JPanel board = new JPanel(new BorderLayout());
        board.setBackground(new Color(249, 251, 255));
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));
        board.setMinimumSize(new Dimension(0, 420));
        board.setPreferredSize(new Dimension(0, 520));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(new Color(249, 251, 255));
        testSuiteRunnerStatusLabel = createMetricValue("Import or create a Test Suite Runner workbook to view test steps.",
                new Color(95, 103, 120));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(new Color(249, 251, 255));
        JButton checkAllButton = createSecondaryButton("Check All");
        JButton uncheckAllButton = createSecondaryButton("Uncheck All");
        openTestSuiteReportButton = createSecondaryButton("Open Report");
        openTestSuiteReportButton.setEnabled(false);
        runTestSuiteButton = createPrimaryButton("Run");
        checkAllButton.addActionListener(e -> setAllRunnerStepSelections(true));
        uncheckAllButton.addActionListener(e -> setAllRunnerStepSelections(false));
        openTestSuiteReportButton.addActionListener(e -> openLastTestSuiteReport());
        runTestSuiteButton.addActionListener(e -> runSelectedTestSuiteSteps());
        actions.add(checkAllButton);
        actions.add(uncheckAllButton);
        actions.add(openTestSuiteReportButton);
        actions.add(runTestSuiteButton);

        toolbar.add(testSuiteRunnerStatusLabel, BorderLayout.WEST);
        toolbar.add(actions, BorderLayout.EAST);

        JPanel runnerOptions = createRunnerExecutionOptionsPanel();
        JPanel boardHeader = new JPanel(new BorderLayout(0, 10));
        boardHeader.setBackground(new Color(249, 251, 255));
        boardHeader.add(toolbar, BorderLayout.NORTH);
        boardHeader.add(runnerOptions, BorderLayout.CENTER);

        testSuiteRunnerStepsTableModel = new DefaultTableModel(
                new String[]{"Run", "Test Suite", "Test Case", "Test Step", "Step Type", "Method", "Endpoint"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        testSuiteRunnerStepsTable = new JTable(testSuiteRunnerStepsTableModel);
        testSuiteRunnerStepsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        testSuiteRunnerStepsTable.setRowHeight(28);
        testSuiteRunnerStepsTable.getColumnModel().getColumn(0).setMaxWidth(70);

        board.add(boardHeader, BorderLayout.NORTH);
        board.add(new JScrollPane(testSuiteRunnerStepsTable), BorderLayout.CENTER);
        return board;
    }

    private JPanel createRunnerExecutionOptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(new Color(249, 251, 255));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setBackground(new Color(249, 251, 255));

        JLabel parallelLabel = createMetricValue("Parallel Execution", new Color(35, 44, 58));
        parallelExecutionSwitch = new JToggleButton("Off");
        parallelExecutionSwitch.setFont(UI_FONT);
        parallelExecutionSwitch.setFocusPainted(false);
        parallelExecutionSwitch.addActionListener(e ->
                parallelExecutionSwitch.setText(parallelExecutionSwitch.isSelected() ? "On" : "Off"));

        threadCountField = new JTextField("1", 4);
        threadCountField.setFont(UI_FONT);
        threadCountField.setHorizontalAlignment(SwingConstants.CENTER);

        controls.add(parallelLabel);
        controls.add(parallelExecutionSwitch);
        controls.add(createMetricValue("Thread Count", new Color(35, 44, 58)));
        controls.add(threadCountField);

        JLabel note = createMetricValue(
                "Note: Test Step inter-dependency on variables is taken into account while running Test Steps in parallel.",
                new Color(95, 103, 120));

        panel.add(controls, BorderLayout.NORTH);
        panel.add(note, BorderLayout.CENTER);
        return panel;
    }

    private void setAllRunnerStepSelections(boolean selected) {
        if (testSuiteRunnerStepsTableModel == null) {
            return;
        }
        for (int row = 0; row < testSuiteRunnerStepsTableModel.getRowCount(); row++) {
            testSuiteRunnerStepsTableModel.setValueAt(selected, row, 0);
        }
    }

    private void runSelectedTestSuiteSteps() {
        Path workbookPath = currentTestSuiteWorkbookPath();
        if (workbookPath == null) {
            JOptionPane.showMessageDialog(this, "Import or create a Test Suite Runner workbook first.",
                    "Test Suite Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Integer> selectedRows = selectedRunnerStepRows();
        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Check at least one test step before running.",
                    "Test Suite Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int threadCount = runnerThreadCount();
        boolean parallelExecution = parallelExecutionSwitch != null
                && parallelExecutionSwitch.isSelected()
                && threadCount > 0;

        runTestSuiteButton.setEnabled(false);
        openTestSuiteReportButton.setEnabled(false);
        testSuiteRunnerStatusLabel.setText("Running " + selectedRows.size() + " test step(s)"
                + (parallelExecution ? " in parallel with " + threadCount + " thread(s)..." : "..."));
        testSuiteRunnerStatusLabel.setForeground(PRIMARY);

        SwingWorker<Path, Void> worker = new SwingWorker<>() {
            @Override
            protected Path doInBackground() throws Exception {
                List<Map<String, String>> steps = readTestSuiteRunnerSteps(workbookPath);
                List<Map<String, String>> selectedSteps = new ArrayList<>();
                for (int row : selectedRows) {
                    if (row >= 0 && row < steps.size()) {
                        selectedSteps.add(steps.get(row));
                    }
                }
                return executeTestSuiteSteps(workbookPath, selectedSteps, parallelExecution, threadCount);
            }

            @Override
            protected void done() {
                runTestSuiteButton.setEnabled(true);
                try {
                    lastTestSuiteReportPath = get();
                    openTestSuiteReportButton.setEnabled(true);
                    testSuiteRunnerStatusLabel.setText("Run complete. Report: " + lastTestSuiteReportPath.getFileName());
                    testSuiteRunnerStatusLabel.setForeground(SUCCESS);
                    openLastTestSuiteReport();
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    testSuiteRunnerStatusLabel.setText("Run failed: " + cause.getMessage());
                    testSuiteRunnerStatusLabel.setForeground(new Color(196, 70, 54));
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            cause.getMessage(), "Test Suite Run Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private List<Integer> selectedRunnerStepRows() {
        List<Integer> selected = new ArrayList<>();
        if (testSuiteRunnerStepsTableModel == null) {
            return selected;
        }
        for (int row = 0; row < testSuiteRunnerStepsTableModel.getRowCount(); row++) {
            Object value = testSuiteRunnerStepsTableModel.getValueAt(row, 0);
            if (value instanceof Boolean checked && checked) {
                selected.add(row);
            }
        }
        return selected;
    }

    private int runnerThreadCount() {
        if (threadCountField == null) {
            return 1;
        }
        String value = threadCountField.getText() == null ? "" : threadCountField.getText().trim();
        if (value.isBlank()) {
            threadCountField.setText("1");
            return 1;
        }
        try {
            int threadCount = Integer.parseInt(value);
            if (threadCount <= 0) {
                return 0;
            }
            return threadCount;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thread Count must be a positive whole number.",
                    "Invalid Thread Count", JOptionPane.WARNING_MESSAGE);
            threadCountField.setText("1");
            return 1;
        }
    }

    private void openLastTestSuiteReport() {
        if (lastTestSuiteReportPath == null || !Files.exists(lastTestSuiteReportPath)) {
            JOptionPane.showMessageDialog(this, "No test report is available yet.",
                    "Test Suite Report", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Desktop.getDesktop().browse(lastTestSuiteReportPath.toUri());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Open Report Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTestSuiteRunnerSteps(Path workbookPath) {
        if (testSuiteRunnerStepsTableModel == null) {
            return;
        }
        testSuiteRunnerStepsTableModel.setRowCount(0);
        try {
            List<Map<String, String>> steps = readTestSuiteRunnerSteps(workbookPath);
            for (Map<String, String> step : steps) {
                JSONObject request = parseOptionalJsonObject(step.get("Hit Request"));
                JSONObject webTest = parseOptionalJsonObject(step.get("WEB_TEST"));
                JSONObject performanceTest = parseOptionalJsonObject(step.get("PERFORMANCE_TEST"));
                boolean webStep = webTest.length() > 0;
                boolean performanceStep = performanceTest.length() > 0;
                testSuiteRunnerStepsTableModel.addRow(new Object[]{
                        true,
                        step.getOrDefault("Test Suite", ""),
                        step.getOrDefault("Test Case", ""),
                        step.getOrDefault("Test Step", ""),
                        runnerStepType(step),
                        webStep ? "WEB" : request.optString("method", ""),
                        webStep
                                ? webTest.optString("startUrl", "")
                                : performanceStep
                                    ? request.optString("endpoint", "") + " ("
                                            + performanceTest.optInt("threads", 1) + "x"
                                            + performanceTest.optInt("iterationsPerThread", 1) + ")"
                                    : request.optString("endpoint", "")
                });
            }
            if (testSuiteRunnerStatusLabel != null) {
                testSuiteRunnerStatusLabel.setText(steps.isEmpty()
                        ? "No test steps found in the imported workbook."
                        : "Loaded " + steps.size() + " test step(s).");
                testSuiteRunnerStatusLabel.setForeground(steps.isEmpty() ? new Color(95, 103, 120) : SUCCESS);
            }
        } catch (Exception e) {
            if (testSuiteRunnerStatusLabel != null) {
                testSuiteRunnerStatusLabel.setText("Could not load test steps: " + e.getMessage());
                testSuiteRunnerStatusLabel.setForeground(new Color(196, 70, 54));
            }
        }
    }

    private List<Map<String, String>> readTestSuiteRunnerSteps(Path workbookPath) throws Exception {
        Map<String, byte[]> entries = new LinkedHashMap<>();
        try (ZipFile workbookZip = new ZipFile(workbookPath.toFile())) {
            var zipEntries = workbookZip.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = zipEntries.nextElement();
                if (!entry.isDirectory()) {
                    try (var input = workbookZip.getInputStream(entry)) {
                        entries.put(entry.getName(), input.readAllBytes());
                    }
                }
            }
        }

        byte[] sheetBytes = entries.get("xl/worksheets/sheet1.xml");
        if (sheetBytes == null) {
            return List.of();
        }
        String sheetXml = new String(sheetBytes, StandardCharsets.UTF_8);
        List<String> sharedStrings = readSharedStrings(entries);
        List<List<String>> rows = readSheetRows(sheetXml, sharedStrings);

        List<String> header = null;
        List<Map<String, String>> steps = new ArrayList<>();
        for (List<String> row : rows) {
            if (header == null) {
                if (isRunnerHeader(row)) {
                    header = row;
                }
                continue;
            }
            if (isBlankRow(row) || isRunnerHeader(row)) {
                continue;
            }
            Map<String, String> step = new LinkedHashMap<>();
            for (int column = 0; column < header.size(); column++) {
                step.put(header.get(column), column < row.size() ? row.get(column) : "");
            }
            if (!step.getOrDefault("Test Step", "").isBlank()) {
                steps.add(step);
            }
        }
        return steps;
    }

    private Path executeTestSuiteSteps(Path workbookPath, List<Map<String, String>> steps,
                                       boolean parallelExecution, int threadCount) throws Exception {
        if (parallelExecution && threadCount > 0) {
            return executeTestSuiteStepsParallel(workbookPath, steps, threadCount);
        }
        return executeTestSuiteStepsSequential(workbookPath, steps);
    }

    private Path executeTestSuiteStepsSequential(Path workbookPath, List<Map<String, String>> steps) throws Exception {
        List<RunnerStepReport> reports = new ArrayList<>();
        Map<String, String> suiteVariables = new HashMap<>();
        int passed = 0;
        int failed = 0;
        for (Map<String, String> step : steps) {
            RunnerStepReport report = executeRunnerStep(workbookPath, step, suiteVariables);
            reports.add(report);
            if (report.passed) {
                passed++;
            } else {
                failed++;
            }
        }

        return writeTestSuiteReport(workbookPath, reports, passed, failed);
    }

    private Path executeTestSuiteStepsParallel(Path workbookPath, List<Map<String, String>> steps,
                                               int threadCount) throws Exception {
        Map<String, String> suiteVariables = new ConcurrentHashMap<>();
        Map<Integer, Set<Integer>> dependencies = runnerStepDependencies(steps);
        Map<Integer, RunnerStepReport> reportsByIndex = new ConcurrentHashMap<>();
        Set<Integer> submitted = new HashSet<>();
        Set<Integer> completed = new HashSet<>();
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(1, threadCount));
        ExecutorCompletionService<RunnerIndexedStepReport> completionService = new ExecutorCompletionService<>(executor);

        try {
            while (completed.size() < steps.size()) {
                boolean submittedReadyStep = false;
                for (int index = 0; index < steps.size(); index++) {
                    if (submitted.contains(index)) {
                        continue;
                    }
                    if (!completed.containsAll(dependencies.getOrDefault(index, Set.of()))) {
                        continue;
                    }
                    final int stepIndex = index;
                    final Map<String, String> step = steps.get(index);
                    completionService.submit(() ->
                            new RunnerIndexedStepReport(stepIndex, executeRunnerStep(workbookPath, step, suiteVariables)));
                    submitted.add(index);
                    submittedReadyStep = true;
                }

                if (submitted.size() == completed.size() && !submittedReadyStep) {
                    throw new IllegalStateException("Unable to schedule test steps because variable dependencies could not be resolved.");
                }

                RunnerIndexedStepReport completedReport = completionService.take().get();
                reportsByIndex.put(completedReport.index(), completedReport.report());
                completed.add(completedReport.index());
            }
        } finally {
            executor.shutdownNow();
        }

        List<RunnerStepReport> reports = new ArrayList<>();
        for (int index = 0; index < steps.size(); index++) {
            RunnerStepReport report = reportsByIndex.get(index);
            if (report != null) {
                reports.add(report);
            }
        }
        int passed = (int) reports.stream().filter(report -> report.passed).count();
        int failed = reports.size() - passed;
        return writeTestSuiteReport(workbookPath, reports, passed, failed);
    }

    private Path writeTestSuiteReport(Path workbookPath, List<RunnerStepReport> reports,
                                      int passed, int failed) throws Exception {
        Path reportDirectory = resolveTestSuiteOutputDirectory().resolve("Reports");
        Files.createDirectories(reportDirectory);
        String reportName = sanitizeWorkbookFileName(testSuiteNameField.getText().trim())
                + "-report-" + System.currentTimeMillis() + ".html";
        Path reportPath = reportDirectory.resolve(reportName).toAbsolutePath();
        Files.writeString(reportPath, buildTestSuiteReportHtml(workbookPath, reports, passed, failed),
                StandardCharsets.UTF_8);
        return reportPath;
    }

    private Map<Integer, Set<Integer>> runnerStepDependencies(List<Map<String, String>> steps) {
        Map<Integer, Set<Integer>> dependencies = new HashMap<>();
        Map<String, Integer> latestProducerByVariable = new HashMap<>();
        for (int index = 0; index < steps.size(); index++) {
            Map<String, String> step = steps.get(index);
            Set<Integer> stepDependencies = new HashSet<>();
            for (String variableName : runnerStepRequiredVariableNames(step)) {
                Integer producerIndex = latestProducerByVariable.get(variableName);
                if (producerIndex != null) {
                    stepDependencies.add(producerIndex);
                }
            }
            dependencies.put(index, stepDependencies);
            for (String variableName : runnerStepCapturedVariableNames(step)) {
                latestProducerByVariable.put(variableName, index);
            }
        }
        return dependencies;
    }

    private Set<String> runnerStepRequiredVariableNames(Map<String, String> step) {
        Set<String> variableNames = new HashSet<>();
        java.util.regex.Pattern variablePattern = java.util.regex.Pattern.compile("\\$\\{([^}]+)}");
        for (String value : step.values()) {
            if (value == null || value.isBlank()) {
                continue;
            }
            java.util.regex.Matcher matcher = variablePattern.matcher(value);
            while (matcher.find()) {
                String variableName = normalizeVariableName(matcher.group(1));
                if (!variableName.isBlank()) {
                    variableNames.add(variableName);
                }
            }
        }
        variableNames.removeAll(runnerVariables(step).keySet());
        return variableNames;
    }

    private Set<String> runnerStepCapturedVariableNames(Map<String, String> step) {
        Set<String> variableNames = new HashSet<>();
        JSONArray captures = parseOptionalJsonArray(step.get("Captured Variables"));
        for (int i = 0; i < captures.length(); i++) {
            JSONObject capture = captures.optJSONObject(i);
            if (capture == null) {
                continue;
            }
            String variableName = normalizeVariableName(capture.optString("variableName"));
            if (!variableName.isBlank()) {
                variableNames.add(variableName);
            }
        }

        JSONObject webTest = parseOptionalJsonObject(step.get("WEB_TEST"));
        JSONArray webSteps = webTest.optJSONArray("steps");
        if (webSteps != null) {
            for (int i = 0; i < webSteps.length(); i++) {
                JSONObject webStep = webSteps.optJSONObject(i);
                if (webStep == null || !isGetTextWebAction(webStep.optString("action"))) {
                    continue;
                }
                String variableName = normalizeVariableName(webStep.optString("value"));
                if (!variableName.isBlank()) {
                    variableNames.add(variableName);
                }
            }
        }
        return variableNames;
    }

    private RunnerStepReport executeRunnerStep(Path workbookPath, Map<String, String> step, Map<String, String> suiteVariables) {
        RunnerStepReport report = new RunnerStepReport();
        report.testSuite = step.getOrDefault("Test Suite", "");
        report.testCase = step.getOrDefault("Test Case", "");
        report.testStep = step.getOrDefault("Test Step", "");
        report.stepType = runnerStepType(step);

        try {
            Map<String, String> variables = new HashMap<>(suiteVariables);
            runnerVariables(step).forEach(variables::putIfAbsent);
            if ("WEB_TEST".equals(report.stepType)) {
                runWebTestStep(report, step.get("WEB_TEST"), variables);
                suiteVariables.putAll(variables);
                report.passed = report.validations.stream().allMatch(validation -> validation.passed);
                return report;
            }
            if ("PERFORMANCE_TEST".equals(report.stepType)) {
                runPerformanceTestStep(report, step, variables);
                suiteVariables.putAll(variables);
                report.passed = report.validations.stream().allMatch(validation -> validation.passed);
                return report;
            }

            JSONObject requestJson = parseOptionalJsonObject(step.get("Hit Request"));
            ApiResponse response = apiService.sendRequest(buildRunnerApiRequest(requestJson,
                    step.getOrDefault("Request Payload", ""), variables));
            report.statusCode = response.statusCode;
            report.timeMs = response.timeMs;
            captureRunnerVariables(step.get("Captured Variables"), response.rawBody, variables);
            suiteVariables.putAll(variables);

            if (!step.getOrDefault("API_FIELD_VALIDATION", "").isBlank()) {
                runApiFieldValidationStep(report, step.get("API_FIELD_VALIDATION"), response.rawBody, variables);
            }
            if (!step.getOrDefault("JSON_COMPARE", "").isBlank()) {
                runJsonCompareStep(workbookPath, report, step.get("JSON_COMPARE"), response.rawBody);
            }
            if ("DB_VALIDATION".equals(report.stepType)) {
                runDbValidationStep(workbookPath, report, step, response.rawBody, variables);
            }
            if (report.validations.isEmpty()) {
                report.addValidation("Request", "HTTP request executed", "", String.valueOf(response.statusCode),
                        response.statusCode >= 200 && response.statusCode < 400, response.statusLine);
            }
        } catch (Exception e) {
            report.addValidation("Step Error", "Execution failed", "", "", false,
                    e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
        }
        report.passed = report.validations.stream().allMatch(validation -> validation.passed);
        return report;
    }

    private ApiRequest buildRunnerApiRequest(JSONObject requestJson, String payload, Map<String, String> variables) {
        ApiRequest request = new ApiRequest();
        request.method = requestJson.optString("method", "GET");
        request.url = normalizeEndpointUrl(resolveRunnerVariables(requestJson.optString("endpoint"), variables));
        request.body = resolveRunnerVariables(payload, variables);
        request.headers = parseHeaders(resolveRunnerVariables(requestJson.optString("headersText"), variables));
        JSONObject authorization = requestJson.optJSONObject("authorization");
        String authType = authorization == null ? requestJson.optString("authType") : authorization.optString("type");
        request.token = "Bearer Token".equals(authType) && authorization != null
                ? resolveRunnerVariables(authorization.optString("token"), variables)
                : "";
        return request;
    }

    private Map<String, String> runnerVariables(Map<String, String> step) {
        Map<String, String> variables = new HashMap<>();
        JSONObject dependencies = parseOptionalJsonObject(step.get("Variable Dependencies"));
        for (String name : dependencies.keySet()) {
            JSONObject dependency = dependencies.optJSONObject(name);
            if (dependency == null) {
                continue;
            }
            if ("runtime".equals(dependency.optString("source"))) {
                variables.put(name, generateRuntimeVariableValue(dependency.optString("generator", name)));
            } else {
                variables.put(name, dependency.optString("value"));
            }
        }
        return variables;
    }

    private String resolveRunnerVariables(String text, Map<String, String> variables) {
        if (text == null || text.isBlank()) {
            return text == null ? "" : text;
        }
        String resolved = text;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            resolved = resolved.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return resolved;
    }

    private void captureRunnerVariables(String capturesJson, String responseBody, Map<String, String> variables) {
        JSONArray captures = parseOptionalJsonArray(capturesJson);
        if (captures.isEmpty() || responseBody == null || responseBody.isBlank()) {
            return;
        }
        Object responseJson = new JSONTokener(responseBody).nextValue();
        for (int i = 0; i < captures.length(); i++) {
            JSONObject capture = captures.optJSONObject(i);
            if (capture == null) {
                continue;
            }
            String variableName = normalizeVariableName(capture.optString("variableName"));
            if (variableName.isBlank()) {
                continue;
            }
            Object value = extractJsonPathValue(responseJson, capture.optString("jsonPath"));
            variables.put(variableName, value == null || value == JSONObject.NULL ? "" : String.valueOf(value));
        }
    }

    private List<List<String>> readSheetRows(String sheetXml, List<String> sharedStrings) {
        List<List<String>> rows = new ArrayList<>();
        java.util.regex.Matcher rowMatcher = java.util.regex.Pattern
                .compile("<row\\b[^>]*>(.*?)</row>", java.util.regex.Pattern.DOTALL)
                .matcher(sheetXml);
        while (rowMatcher.find()) {
            rows.add(rowValues(rowMatcher.group(1), sharedStrings));
        }
        return rows;
    }

    private boolean isBlankRow(List<String> row) {
        return row.stream().allMatch(value -> value == null || value.isBlank());
    }

    private JSONObject parseOptionalJsonObject(String value) {
        if (value == null || value.isBlank()) {
            return new JSONObject();
        }
        try {
            return new JSONObject(value);
        } catch (Exception ignored) {
            return new JSONObject();
        }
    }

    private String runnerStepType(Map<String, String> step) {
        if (!step.getOrDefault("DB_CONNECTION", "").isBlank()
                || !step.getOrDefault("DB_QUERY", "").isBlank()
                || !step.getOrDefault("API_DB_VALIDATION", "").isBlank()
                || !step.getOrDefault("DB_COLUMN_VALIDATION", "").isBlank()) {
            return "DB_VALIDATION";
        }
        if (!step.getOrDefault("DB_VALIDATION", "").isBlank()) {
            return "DB_VALIDATION";
        }
        if (!step.getOrDefault("WEB_TEST", "").isBlank()) {
            return "WEB_TEST";
        }
        if (!step.getOrDefault("PERFORMANCE_TEST", "").isBlank()) {
            return "PERFORMANCE_TEST";
        }
        if (!step.getOrDefault("JSON_COMPARE", "").isBlank()) {
            return "JSON_COMPARE";
        }
        if (!step.getOrDefault("API_FIELD_VALIDATION", "").isBlank()) {
            return "API_FIELD_VALIDATION";
        }
        return "";
    }

    private void runWebTestStep(RunnerStepReport report, String webTestJson, Map<String, String> variables) throws Exception {
        JSONObject config = parseOptionalJsonObject(webTestJson);
        WebTestCase testCase = new WebTestCase();
        testCase.testName = resolveRunnerVariables(config.optString("testName", report.testStep), variables);
        testCase.startUrl = resolveRunnerVariables(config.optString("startUrl"), variables);

        JSONArray steps = config.optJSONArray("steps");
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("WEB_TEST step does not contain recorded web steps.");
        }
        for (int i = 0; i < steps.length(); i++) {
            JSONObject stepJson = steps.optJSONObject(i);
            if (stepJson == null) {
                continue;
            }
            WebTestStep webStep = new WebTestStep();
            webStep.action = stepJson.optString("action");
            webStep.selector = resolveRunnerVariables(stepJson.optString("selector"), variables);
            webStep.value = isGetTextWebAction(webStep.action)
                    ? stepJson.optString("value")
                    : resolveRunnerVariables(stepJson.optString("value"), variables);
            webStep.note = stepJson.optString("note");
            webStep.suggested = stepJson.optBoolean("suggested");
            testCase.steps.add(webStep);
        }

        boolean headless = config.optBoolean("headless", true);
        int slowMoMillis = Math.max(0, config.optInt("slowMoMillis", 0));
        WebTestRunReport webReport = playwrightRecorderController.runTest(testCase, headless, slowMoMillis);
        for (int i = 0; i < webReport.results.size(); i++) {
            WebTestExecutionResult result = webReport.results.get(i);
            storeCapturedWebVariable(result, variables);
            report.addValidation("Step " + (i + 1) + " " + result.action,
                    result.selector == null ? "" : result.selector,
                    result.expectedValue == null ? "" : result.expectedValue,
                    result.passed ? "PASS" : "FAIL",
                    result.passed,
                    result.message == null ? "" : result.message);
        }
        if (webReport.results.isEmpty()) {
            report.addValidation("Web Test", "No web steps executed", "", "", false,
                    webReport.stopped ? "Web test stopped before a step ran." : "No executable web steps were found.");
        }
    }

    private void runPerformanceTestStep(RunnerStepReport report, Map<String, String> step,
                                        Map<String, String> variables) throws Exception {
        JSONObject config = parseOptionalJsonObject(step.get("PERFORMANCE_TEST"));
        if (config.isEmpty()) {
            throw new IllegalArgumentException("PERFORMANCE_TEST step does not contain load test configuration.");
        }

        ApiRequest request = buildRunnerApiRequest(parseOptionalJsonObject(step.get("Hit Request")),
                step.getOrDefault("Request Payload", ""), variables);
        int threads = Math.max(1, config.optInt("threads", 1));
        int iterations = Math.max(1, config.optInt("iterationsPerThread", 1));
        PerformanceTestResult result = performanceTestService.runLoadTest(request, threads, iterations);

        report.statusCode = result.errors == 0 ? 200 : 500;
        report.timeMs = result.duration == null ? 0 : result.duration.toMillis();
        report.performanceReportPath = result.reportIndexPath;
        boolean passed = result.errors == 0;
        report.addValidation("Performance Test", threads + " threads x " + iterations + " iterations",
                "0 errors",
                result.errors + " errors / " + result.samples + " samples",
                passed,
                "HTML report generated: " + (result.reportIndexPath == null ? "" : result.reportIndexPath.toAbsolutePath()));
        report.addValidation("Throughput", "samples per second", "",
                String.format("%.2f/sec", result.throughputPerSecond),
                true,
                "Duration: " + formatDuration(result.duration));
    }

    private void runApiFieldValidationStep(RunnerStepReport report, String validationJson,
                                           String responseBody, Map<String, String> variables) {
        JSONArray validations = parseOptionalJsonObject(validationJson).optJSONArray("validations");
        if (validations == null) {
            validations = parseOptionalJsonArray(validationJson);
        }
        Object responseJson = responseBody == null || responseBody.isBlank()
                ? new JSONObject()
                : new JSONTokener(responseBody).nextValue();
        for (int i = 0; i < validations.length(); i++) {
            JSONObject validation = validations.optJSONObject(i);
            if (validation == null) {
                continue;
            }
            String path = validation.optString("jsonPath");
            Object actual = extractJsonPathValue(responseJson, path);
            String actualValue = actual == null || actual == JSONObject.NULL ? "" : String.valueOf(actual);
            String actualType = jsonValueType(actual);
            String nullRule = validation.optString("nullValidation");
            String typeRule = validation.optString("typeValidation");
            String expected = resolveRunnerVariables(validation.optString("expectedValueOrVariable"), variables);
            List<String> errors = fieldValidationErrors(actualType, actualValue, nullRule, typeRule, expected);
            report.addValidation(path, "Null: " + nullRule + ", Type: " + typeRule,
                    expected, actualValue, errors.isEmpty(), String.join(", ", errors));
        }
    }

    private void runJsonCompareStep(Path workbookPath, RunnerStepReport report, String compareJson, String responseBody) throws Exception {
        JSONObject config = parseOptionalJsonObject(compareJson);
        JSONObject expectedResponse = config.optJSONObject("expectedResponse");
        if (expectedResponse == null) {
            throw new IllegalArgumentException("JSON_COMPARE step does not contain expectedResponse details.");
        }
        Path expectedPath = resolveWorkbookRelativePath(workbookPath,
                expectedResponse.optString("path"), expectedResponse.optString("relativePath"));
        String expected = Files.readString(expectedPath);
        boolean strict = "STRICT".equalsIgnoreCase(config.optString("compareMode"));
        List<Object[]> results = comparator.compare(expected, responseBody, strict, true);
        for (Object[] result : results) {
            String type = String.valueOf(result[0]);
            boolean passed = "Match".equals(type) || "Message".equals(type);
            report.addValidation(String.valueOf(result[1]), "JSON " + type,
                    String.valueOf(result[2]), String.valueOf(result[3]), passed,
                    passed ? "" : "JSON comparison mismatch");
        }
    }

    private void runDbValidationStep(Path workbookPath, RunnerStepReport report, Map<String, String> step,
                                     String responseBody, Map<String, String> variables) throws Exception {
        DbConnectionConfig config = runnerDbConnectionConfig(workbookPath, step.get("DB_CONNECTION"));
        String sqlQuery = resolveRunnerVariables(step.getOrDefault("DB_QUERY", ""), variables);

        JSONArray apiDbValidations = parseOptionalJsonArray(step.get("API_DB_VALIDATION"));
        if (!apiDbValidations.isEmpty()) {
            List<DbValidationRule> rules = new ArrayList<>();
            for (int i = 0; i < apiDbValidations.length(); i++) {
                JSONObject json = apiDbValidations.optJSONObject(i);
                if (json == null) {
                    continue;
                }
                DbValidationRule rule = new DbValidationRule();
                rule.apiField = json.optString("apiField");
                rule.dbColumn = json.optString("dbColumn");
                rule.operator = json.optString("operator", "=");
                rule.description = json.optString("description");
                rules.add(rule);
            }
            if (!rules.isEmpty()) {
                DbValidationReport dbReport = dbValidationService.validate(config, sqlQuery, rules, responseBody, variables);
                for (DbValidationResult result : dbReport.results) {
                    report.addValidation(result.field, "API-DB " + result.operator,
                            result.expectedValue, result.actualValue, result.passed, result.message);
                }
            }
        }

        JSONArray dbColumnValidations = parseOptionalJsonArray(step.get("DB_COLUMN_VALIDATION"));
        if (!dbColumnValidations.isEmpty()) {
            List<Map<String, Object>> rows = dbValidationService.executeQuery(config, sqlQuery, responseBody, variables);
            for (int i = 0; i < dbColumnValidations.length(); i++) {
                JSONObject validation = dbColumnValidations.optJSONObject(i);
                if (validation == null) {
                    continue;
                }
                Object actual = dbColumnActualValue(rows, validation.optString("dbColumnName"));
                String actualType = dbValueType(actual);
                String actualValue = actual == null ? "" : String.valueOf(actual);
                String expected = resolveRunnerVariables(validation.optString("expectedValueOrVariable"), variables);
                List<String> errors = dbColumnValidationErrors(actualType, actualValue,
                        validation.optString("nullValidation"), validation.optString("typeValidation"), expected);
                report.addValidation(validation.optString("dbColumnName"),
                        "DB Column Null: " + validation.optString("nullValidation")
                                + ", Type: " + validation.optString("typeValidation"),
                        expected, actualValue, errors.isEmpty(), String.join(", ", errors));
            }
        }
    }

    private DbConnectionConfig runnerDbConnectionConfig(Path workbookPath, String connectionJson) throws Exception {
        JSONObject connection = parseOptionalJsonObject(connectionJson);
        Path connectionPath = resolveWorkbookRelativePath(workbookPath,
                connection.optString("path"), connection.optString("relativePath"));
        JSONObject json = new JSONObject(Files.readString(connectionPath));
        DbConnectionConfig config = new DbConnectionConfig();
        config.databaseType = json.optString("databaseType", connection.optString("databaseType", "MySQL"));
        config.jdbcUrl = json.optString("jdbcUrl", connection.optString("jdbcUrl"));
        config.username = json.optString("username", connection.optString("username"));
        config.password = json.optString("password");
        config.driverClass = json.optString("driverClass", connection.optString("driverClass"));
        return config;
    }

    private Path resolveWorkbookRelativePath(Path workbookPath, String absolutePath, String relativePath) {
        if (absolutePath != null && !absolutePath.isBlank() && Files.exists(Path.of(absolutePath))) {
            return Path.of(absolutePath);
        }
        Path workbookDirectory = workbookPath.toAbsolutePath().getParent();
        if (workbookDirectory != null && relativePath != null && !relativePath.isBlank()) {
            Path resolved = workbookDirectory.resolve(relativePath).normalize();
            if (Files.exists(resolved)) {
                return resolved;
            }
        }
        return Path.of(absolutePath == null || absolutePath.isBlank() ? relativePath : absolutePath);
    }

    private Object dbColumnActualValue(List<Map<String, Object>> rows, String columnReference) {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("DB query returned no rows.");
        }
        String columnName = columnReference == null ? "" : columnReference.trim();
        int rowIndex = 0;
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("^(.+)\\[(\\d+)]$").matcher(columnName);
        if (matcher.matches()) {
            columnName = matcher.group(1).trim();
            rowIndex = Integer.parseInt(matcher.group(2));
        }
        if (rowIndex >= rows.size()) {
            throw new IllegalArgumentException("DB row index " + rowIndex + " is not available for " + columnName);
        }
        Map<String, Object> row = rows.get(rowIndex);
        if (!row.containsKey(columnName)) {
            throw new IllegalArgumentException("DB column not found: " + columnName);
        }
        return row.get(columnName);
    }

    private List<String> dbColumnValidationErrors(String actualType, String actualValue,
                                                  String nullRule, String typeRule, String expectedValue) {
        List<String> errors = new ArrayList<>();
        boolean isNull = "null".equals(actualType);
        boolean isEmpty = actualValue.isEmpty();
        boolean isBlank = actualValue.isBlank();
        if ("Not Null".equals(nullRule) && isNull) {
            errors.add("expected not null");
        } else if ("Null".equals(nullRule) && !isNull) {
            errors.add("expected null");
        } else if ("Not Empty".equals(nullRule) && (isNull || isEmpty)) {
            errors.add("expected not empty");
        } else if ("Empty".equals(nullRule) && !isEmpty) {
            errors.add("expected empty");
        } else if ("Not Blank".equals(nullRule) && (isNull || isBlank)) {
            errors.add("expected not blank");
        } else if ("Blank".equals(nullRule) && !isBlank) {
            errors.add("expected blank");
        }
        if (!typeRule.isBlank() && !"Skip".equals(typeRule) && !dbTypeMatches(typeRule, actualType, actualValue)) {
            errors.add("expected " + typeRule);
        }
        if (!expectedValue.isBlank() && !expectedValue.equals(actualValue)) {
            errors.add("expected value mismatch");
        }
        return errors;
    }

    private JSONArray parseOptionalJsonArray(String value) {
        if (value == null || value.isBlank()) {
            return new JSONArray();
        }
        try {
            return new JSONArray(value);
        } catch (Exception ignored) {
            return new JSONArray();
        }
    }

    private Object extractJsonPathValue(Object root, String path) {
        String normalized = path == null ? "" : path.trim();
        if (normalized.isEmpty() || "$".equals(normalized)) {
            return root;
        }
        if (normalized.startsWith("$.")) {
            normalized = normalized.substring(2);
        } else if (normalized.startsWith("$")) {
            normalized = normalized.substring(1);
        }
        Object current = root;
        for (String part : normalized.split("\\.")) {
            if (part.isBlank()) {
                continue;
            }
            current = stepIntoJsonPath(current, part);
        }
        return current;
    }

    private Object stepIntoJsonPath(Object current, String part) {
        String remaining = part;
        int bracketIndex = remaining.indexOf('[');
        Object value = bracketIndex <= 0 ? current : objectJsonField(current, remaining.substring(0, bracketIndex));
        if (bracketIndex < 0) {
            return objectJsonField(current, remaining);
        }
        while (bracketIndex >= 0) {
            int closeIndex = remaining.indexOf(']', bracketIndex);
            int index = Integer.parseInt(remaining.substring(bracketIndex + 1, closeIndex).trim());
            if (!(value instanceof JSONArray array)) {
                throw new IllegalArgumentException("Path segment is not an array: " + part);
            }
            value = array.get(index);
            bracketIndex = remaining.indexOf('[', closeIndex);
        }
        return value;
    }

    private Object objectJsonField(Object current, String fieldName) {
        if (fieldName == null || fieldName.isBlank()) {
            return current;
        }
        if (current instanceof JSONObject object) {
            if (!object.has(fieldName)) {
                throw new IllegalArgumentException("Response field not found: " + fieldName);
            }
            return object.get(fieldName);
        }
        if (current instanceof JSONArray array) {
            if (array.isEmpty()) {
                throw new IllegalArgumentException("Response array is empty for field: " + fieldName);
            }
            return objectJsonField(array.get(0), fieldName);
        }
        throw new IllegalArgumentException("Path segment is not an object: " + fieldName);
    }

    private String jsonValueType(Object value) {
        if (value == null || value == JSONObject.NULL) {
            return "null";
        }
        if (value instanceof JSONObject) {
            return "object";
        }
        if (value instanceof JSONArray) {
            return "array";
        }
        if (value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte) {
            return "integer";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        return "string";
    }

    private void createTestSuiteWorkbook() {
        String testSuite = testSuiteNameField.getText().trim();
        String testCase = testCaseNameField.getText().trim();

        if (testSuite.isBlank() || testCase.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Enter both Test Suite and Test Case before creating the workbook.",
                    "Missing Test Suite Details", JOptionPane.WARNING_MESSAGE);
            return;
        }

        createTestSuiteWorkbookButton.setEnabled(false);
        try {
            Path outputDirectory = resolveTestSuiteOutputDirectory();
            Files.createDirectories(outputDirectory);

            String workbookName = sanitizeWorkbookFileName(testSuite);
            String sheetName = createSafeExcelSheetName(testCase);
            Path workbookPath = outputDirectory.resolve(workbookName + ".xlsx").toAbsolutePath();

            writeSingleSheetWorkbook(workbookPath, sheetName, testCase);

            testSuiteWorkbookPathField.setText(workbookPath.toString());
            testSuiteWorkbookPathField.setForeground(new Color(35, 44, 58));
            populateImportedTestSuiteDetails(testSuite, sheetName);
            refreshTestSuiteRunnerSteps(workbookPath);
            JOptionPane.showMessageDialog(this,
                    "Workbook created successfully.",
                    "Test Suite Created", JOptionPane.INFORMATION_MESSAGE);
        } catch (Throwable e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            testSuiteWorkbookPathField.setForeground(new Color(196, 70, 54));
            testSuiteWorkbookPathField.setText("Failed: " + message);
            JOptionPane.showMessageDialog(this, message, "Create Workbook Failed", JOptionPane.ERROR_MESSAGE);
        } finally {
            createTestSuiteWorkbookButton.setEnabled(true);
        }
    }

    private void importTestSuiteWorkbook() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Excel Workbook (*.xlsx)", "xlsx"));
        Path outputDirectory = resolveTestSuiteOutputDirectory();
        if (Files.isDirectory(outputDirectory)) {
            chooser.setCurrentDirectory(outputDirectory.toFile());
        }
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();
        try {
            String workbookName = workbookNameWithoutExtension(selectedFile.getName());
            String sheetName = readFirstWorkbookSheetName(selectedFile.toPath());
            populateImportedTestSuiteDetails(workbookName, sheetName);
            testSuiteWorkbookPathField.setText(selectedFile.getAbsolutePath());
            testSuiteWorkbookPathField.setForeground(new Color(35, 44, 58));
            refreshTestSuiteRunnerSteps(selectedFile.toPath());
            JOptionPane.showMessageDialog(this,
                    "Test suite runner imported successfully.",
                    "Test Suite Imported", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            JOptionPane.showMessageDialog(this, message, "Import Workbook Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateImportedTestSuiteDetails(String testSuite, String testCase) {
        if (testSuiteNameField != null) {
            testSuiteNameField.setText(testSuite);
        }
        if (testCaseNameField != null) {
            testCaseNameField.setText(testCase);
        }
        if (fieldValidationTestSuiteField != null) {
            fieldValidationTestSuiteField.setText(testSuite);
        }
        if (fieldValidationTestCaseField != null) {
            fieldValidationTestCaseField.setText(testCase);
        }
        if (fieldValidationTestStepField != null && fieldValidationTestStepField.getText().trim().isBlank()) {
            fieldValidationTestStepField.setText(testCase + " API Field Validation");
        }
        if (jsonCompareTestSuiteField != null) {
            jsonCompareTestSuiteField.setText(testSuite);
        }
        if (jsonCompareTestCaseField != null) {
            jsonCompareTestCaseField.setText(testCase);
        }
        if (jsonCompareTestStepField != null && jsonCompareTestStepField.getText().trim().isBlank()) {
            jsonCompareTestStepField.setText(testCase + " JSON Compare");
        }
        if (dbValidationTestSuiteField != null) {
            dbValidationTestSuiteField.setText(testSuite);
        }
        if (dbValidationTestCaseField != null) {
            dbValidationTestCaseField.setText(testCase);
        }
        if (dbValidationTestStepField != null && dbValidationTestStepField.getText().trim().isBlank()) {
            dbValidationTestStepField.setText(testCase + " DB Validation");
        }
        if (webTestingTestSuiteField != null) {
            webTestingTestSuiteField.setText(testSuite);
        }
        if (webTestingTestCaseField != null) {
            webTestingTestCaseField.setText(testCase);
        }
        if (webTestingTestStepField != null && webTestingTestStepField.getText().trim().isBlank()) {
            webTestingTestStepField.setText(testCase + " Web Test");
        }
        if (performanceTestSuiteField != null) {
            performanceTestSuiteField.setText(testSuite);
        }
        if (performanceTestCaseField != null) {
            performanceTestCaseField.setText(testCase);
        }
        if (performanceTestStepField != null && performanceTestStepField.getText().trim().isBlank()) {
            performanceTestStepField.setText(testCase + " Performance Test");
        }
    }

    private String workbookNameWithoutExtension(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".xlsx") ? fileName.substring(0, fileName.length() - 5) : fileName;
    }

    private String readFirstWorkbookSheetName(Path workbookPath) throws Exception {
        try (ZipFile workbookZip = new ZipFile(workbookPath.toFile())) {
            ZipEntry workbookEntry = workbookZip.getEntry("xl/workbook.xml");
            if (workbookEntry == null) {
                throw new IllegalArgumentException("Selected file does not contain an Excel workbook definition.");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            Document document;
            try (var workbookXml = workbookZip.getInputStream(workbookEntry)) {
                document = factory.newDocumentBuilder().parse(workbookXml);
            }
            NodeList sheets = document.getElementsByTagNameNS("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheet");
            if (sheets.getLength() == 0) {
                sheets = document.getElementsByTagName("sheet");
            }
            if (sheets.getLength() == 0) {
                throw new IllegalArgumentException("No sheets were found in the selected workbook.");
            }

            String sheetName = sheets.item(0).getAttributes().getNamedItem("name").getNodeValue();
            if (sheetName == null || sheetName.isBlank()) {
                throw new IllegalArgumentException("The first sheet does not have a usable name.");
            }
            return sheetName;
        }
    }

    private void appendRowsToWorkbook(Path workbookPath, List<List<String>> rows) throws Exception {
        if (rows.isEmpty()) {
            return;
        }

        Map<String, byte[]> entries = new LinkedHashMap<>();
        try (ZipFile workbookZip = new ZipFile(workbookPath.toFile())) {
            ZipEntry sheetEntry = workbookZip.getEntry("xl/worksheets/sheet1.xml");
            if (sheetEntry == null) {
                throw new IllegalArgumentException("The selected workbook does not contain xl/worksheets/sheet1.xml.");
            }
            var zipEntries = workbookZip.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = zipEntries.nextElement();
                if (!entry.isDirectory()) {
                    try (var input = workbookZip.getInputStream(entry)) {
                        entries.put(entry.getName(), input.readAllBytes());
                    }
                }
            }
        }

        String sheetXml = new String(entries.get("xl/worksheets/sheet1.xml"), StandardCharsets.UTF_8);
        List<List<String>> rowsToAppend = new ArrayList<>();
        List<String> sharedStrings = readSharedStrings(entries);
        boolean needsRunnerHeader = !hasRunnerHeaderRow(sheetXml, sharedStrings);
        if (needsRunnerHeader) {
            rowsToAppend.add(runnerWorkbookHeaderColumns());
        } else {
            sheetXml = ensureRunnerHeaderColumns(sheetXml, sharedStrings, runnerWorkbookOptionalColumns());
        }
        rowsToAppend.addAll(rows);

        String updatedSheetXml = appendInlineStringRows(sheetXml, rowsToAppend, needsRunnerHeader);
        entries.put("xl/worksheets/sheet1.xml", updatedSheetXml.getBytes(StandardCharsets.UTF_8));
        ensureRunnerWorkbookStyles(entries);

        Path tempWorkbook = Files.createTempFile(workbookPath.getParent(), "testweave-runner-", ".xlsx");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(tempWorkbook))) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey()));
                zip.write(entry.getValue());
                zip.closeEntry();
            }
        }
        Files.move(tempWorkbook, workbookPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private String appendInlineStringRows(String sheetXml, List<List<String>> rows, boolean firstRowIsHeader) {
        int nextRow = findMaxSheetRow(sheetXml) + 1;
        StringBuilder rowXml = new StringBuilder();
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            List<String> row = rows.get(rowIndex);
            boolean headerRow = firstRowIsHeader && rowIndex == 0;
            rowXml.append("                        <row r=\"").append(nextRow).append("\">\n");
            for (int column = 0; column < row.size(); column++) {
                rowXml.append("                          <c r=\"")
                        .append(excelColumnName(column + 1))
                        .append(nextRow)
                        .append("\"")
                        .append(headerRow ? " s=\"1\"" : "")
                        .append(" t=\"inlineStr\"><is><t>")
                        .append(escapeXml(row.get(column) == null ? "" : row.get(column)))
                        .append("</t></is></c>\n");
            }
            rowXml.append("                        </row>\n");
            nextRow++;
        }

        if (sheetXml.contains("</sheetData>")) {
            return sheetXml.replace("</sheetData>", rowXml + "                      </sheetData>");
        }
        if (sheetXml.contains("<sheetData/>")) {
            return sheetXml.replace("<sheetData/>", "<sheetData>\n" + rowXml + "                      </sheetData>");
        }
        return sheetXml.replace("</worksheet>", "                      <sheetData>\n"
                + rowXml + "                      </sheetData>\n                    </worksheet>");
    }

    private String ensureRunnerHeaderColumns(String sheetXml, List<String> sharedStrings, List<String> requiredColumns) {
        java.util.regex.Matcher rowMatcher = java.util.regex.Pattern
                .compile("<row\\b[^>]*>(.*?)</row>", java.util.regex.Pattern.DOTALL)
                .matcher(sheetXml);
        StringBuffer updated = new StringBuffer();
        boolean changed = false;
        while (rowMatcher.find()) {
            String rowXml = rowMatcher.group(0);
            String rowBody = rowMatcher.group(1);
            List<String> values = rowValues(rowBody, sharedStrings);
            if (!changed && isRunnerHeader(values)) {
                int rowNumber = rowNumber(rowXml);
                StringBuilder missingCells = new StringBuilder();
                int columnCount = values.size();
                for (String requiredColumn : requiredColumns) {
                    if (values.contains(requiredColumn)) {
                        continue;
                    }
                    columnCount++;
                    missingCells.append("                          <c r=\"")
                            .append(excelColumnName(columnCount))
                            .append(rowNumber)
                            .append("\" s=\"1\" t=\"inlineStr\"><is><t>")
                            .append(escapeXml(requiredColumn))
                            .append("</t></is></c>\n");
                }
                String replacement = missingCells.isEmpty()
                        ? rowXml
                        : rowXml.replace("</row>", missingCells + "                        </row>");
                rowMatcher.appendReplacement(updated, java.util.regex.Matcher.quoteReplacement(replacement));
                changed = !missingCells.isEmpty();
            } else {
                rowMatcher.appendReplacement(updated, java.util.regex.Matcher.quoteReplacement(rowXml));
            }
        }
        rowMatcher.appendTail(updated);
        return changed ? updated.toString() : sheetXml;
    }

    private List<String> runnerWorkbookHeaderColumns() {
        return List.of(
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
                "PERFORMANCE_TEST"
        );
    }

    private List<String> runnerWorkbookOptionalColumns() {
        return List.of(
                "JSON_COMPARE",
                "DB_VALIDATION",
                "DB_CONNECTION",
                "DB_QUERY",
                "API_DB_VALIDATION",
                "DB_COLUMN_VALIDATION",
                "WEB_TEST",
                "PERFORMANCE_TEST"
        );
    }

    private List<String> rowValues(String rowXml, List<String> sharedStrings) {
        List<String> values = new ArrayList<>();
        java.util.regex.Matcher cellMatcher = java.util.regex.Pattern
                .compile("<c\\b([^>]*)>(.*?)</c>", java.util.regex.Pattern.DOTALL)
                .matcher(rowXml);
        while (cellMatcher.find()) {
            values.add(cellText(cellMatcher.group(1), cellMatcher.group(2), sharedStrings));
        }
        return values;
    }

    private int rowNumber(String rowXml) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("<row\\b[^>]*\\sr=\"(\\d+)\"")
                .matcher(rowXml);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 1;
    }

    private List<String> readSharedStrings(Map<String, byte[]> entries) {
        byte[] sharedStringsBytes = entries.get("xl/sharedStrings.xml");
        if (sharedStringsBytes == null) {
            return List.of();
        }
        String sharedStringsXml = new String(sharedStringsBytes, StandardCharsets.UTF_8);
        List<String> values = new ArrayList<>();
        java.util.regex.Matcher stringMatcher = java.util.regex.Pattern
                .compile("<si\\b[^>]*>(.*?)</si>", java.util.regex.Pattern.DOTALL)
                .matcher(sharedStringsXml);
        while (stringMatcher.find()) {
            String itemXml = stringMatcher.group(1);
            StringBuilder value = new StringBuilder();
            java.util.regex.Matcher textMatcher = java.util.regex.Pattern
                    .compile("<t[^>]*>(.*?)</t>", java.util.regex.Pattern.DOTALL)
                    .matcher(itemXml);
            while (textMatcher.find()) {
                value.append(unescapeXml(textMatcher.group(1)));
            }
            values.add(value.toString());
        }
        return values;
    }

    private boolean hasRunnerHeaderRow(String sheetXml, List<String> sharedStrings) {
        java.util.regex.Matcher rowMatcher = java.util.regex.Pattern
                .compile("<row\\b[^>]*>(.*?)</row>", java.util.regex.Pattern.DOTALL)
                .matcher(sheetXml);
        while (rowMatcher.find()) {
            String rowXml = rowMatcher.group(1);
            List<String> values = rowValues(rowXml, sharedStrings);
            if (isRunnerHeader(values)) {
                return true;
            }
        }
        return false;
    }

    private String cellText(String attributes, String cellXml, List<String> sharedStrings) {
        java.util.regex.Matcher inlineMatcher = java.util.regex.Pattern
                .compile("<is>\\s*<t[^>]*>(.*?)</t>\\s*</is>", java.util.regex.Pattern.DOTALL)
                .matcher(cellXml);
        if (inlineMatcher.find()) {
            return unescapeXml(inlineMatcher.group(1));
        }

        java.util.regex.Matcher valueMatcher = java.util.regex.Pattern
                .compile("<v>(.*?)</v>", java.util.regex.Pattern.DOTALL)
                .matcher(cellXml);
        if (!valueMatcher.find()) {
            return "";
        }

        String value = valueMatcher.group(1).trim();
        if (attributes.contains("t=\"s\"")) {
            try {
                int sharedStringIndex = Integer.parseInt(value);
                if (sharedStringIndex >= 0 && sharedStringIndex < sharedStrings.size()) {
                    return sharedStrings.get(sharedStringIndex);
                }
            } catch (NumberFormatException ignored) {
                return "";
            }
        }
        return unescapeXml(value);
    }

    private boolean isRunnerHeader(List<String> values) {
        return values.size() >= 6
                && "Test Suite".equals(values.get(0))
                && "Test Case".equals(values.get(1))
                && "Test Step".equals(values.get(2))
                && "Hit Request".equals(values.get(3))
                && (("Request Payload".equals(values.get(4))
                        && values.size() >= 7
                        && "Captured Variables".equals(values.get(5))
                        && "API_FIELD_VALIDATION".equals(values.get(6)))
                    || ("Captured Variables".equals(values.get(4))
                        && "API_FIELD_VALIDATION".equals(values.get(5))));
    }

    private String unescapeXml(String value) {
        return value
                .replace("&apos;", "'")
                .replace("&quot;", "\"")
                .replace("&gt;", ">")
                .replace("&lt;", "<")
                .replace("&amp;", "&");
    }

    private void ensureRunnerWorkbookStyles(Map<String, byte[]> entries) {
        entries.put("xl/styles.xml", runnerWorkbookStylesXml().getBytes(StandardCharsets.UTF_8));

        String contentTypes = new String(entries.getOrDefault("[Content_Types].xml", new byte[0]), StandardCharsets.UTF_8);
        if (!contentTypes.contains("PartName=\"/xl/styles.xml\"")) {
            contentTypes = contentTypes.replace("</Types>",
                    "  <Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>\n</Types>");
            entries.put("[Content_Types].xml", contentTypes.getBytes(StandardCharsets.UTF_8));
        }

        String relsPath = "xl/_rels/workbook.xml.rels";
        String rels = new String(entries.getOrDefault(relsPath, new byte[0]), StandardCharsets.UTF_8);
        if (!rels.contains("officeDocument/2006/relationships/styles")) {
            rels = rels.replace("</Relationships>",
                    "  <Relationship Id=\"rIdStyles\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>\n</Relationships>");
            entries.put(relsPath, rels.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String runnerWorkbookStylesXml() {
        return """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
                  <fonts count="2">
                    <font><sz val="11"/><color theme="1"/><name val="Calibri"/><family val="2"/></font>
                    <font><b/><sz val="11"/><color rgb="FF000000"/><name val="Calibri"/><family val="2"/></font>
                  </fonts>
                  <fills count="3">
                    <fill><patternFill patternType="none"/></fill>
                    <fill><patternFill patternType="gray125"/></fill>
                    <fill><patternFill patternType="solid"><fgColor rgb="FFFFC000"/><bgColor indexed="64"/></patternFill></fill>
                  </fills>
                  <borders count="1">
                    <border><left/><right/><top/><bottom/><diagonal/></border>
                  </borders>
                  <cellStyleXfs count="1">
                    <xf numFmtId="0" fontId="0" fillId="0" borderId="0"/>
                  </cellStyleXfs>
                  <cellXfs count="2">
                    <xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/>
                    <xf numFmtId="0" fontId="1" fillId="2" borderId="0" xfId="0" applyFont="1" applyFill="1"/>
                  </cellXfs>
                  <cellStyles count="1">
                    <cellStyle name="Normal" xfId="0" builtinId="0"/>
                  </cellStyles>
                  <dxfs count="0"/>
                  <tableStyles count="0" defaultTableStyle="TableStyleMedium2" defaultPivotStyle="PivotStyleLight16"/>
                </styleSheet>
                """;
    }

    private int findMaxSheetRow(String sheetXml) {
        int max = 0;
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("<row[^>]*\\sr=\"(\\d+)\"").matcher(sheetXml);
        while (matcher.find()) {
            max = Math.max(max, Integer.parseInt(matcher.group(1)));
        }
        return max;
    }

    private String excelColumnName(int columnNumber) {
        StringBuilder name = new StringBuilder();
        int column = columnNumber;
        while (column > 0) {
            column--;
            name.insert(0, (char) ('A' + (column % 26)));
            column /= 26;
        }
        return name.toString();
    }

    private Path resolveTestSuiteOutputDirectory() {
        Path documents = Path.of(System.getProperty("user.home"), "Documents");
        Path baseDirectory = Files.isDirectory(documents)
                ? documents.resolve(APP_NAME)
                : Path.of(System.getProperty("user.home"), APP_NAME);
        return baseDirectory.resolve("TestSuites");
    }

    private void writeSingleSheetWorkbook(Path workbookPath, String sheetName, String testCase) throws Exception {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(workbookPath))) {
            writeZipEntry(zip, "[Content_Types].xml", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
                      <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
                      <Default Extension="xml" ContentType="application/xml"/>
                      <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
                      <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
                      <Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
                    </Types>
                    """);
            writeZipEntry(zip, "_rels/.rels", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                      <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
                    </Relationships>
                    """);
            writeZipEntry(zip, "xl/_rels/workbook.xml.rels", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                      <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
                      <Relationship Id="rIdStyles" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
                    </Relationships>
                    """);
            writeZipEntry(zip, "xl/styles.xml", runnerWorkbookStylesXml());
            writeZipEntry(zip, "xl/workbook.xml", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
                      <sheets>
                        <sheet name="%s" sheetId="1" r:id="rId1"/>
                      </sheets>
                    </workbook>
                    """.formatted(escapeXml(sheetName)));
            writeZipEntry(zip, "xl/worksheets/sheet1.xml", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
                      <sheetData>
                        <row r="1">
                          <c r="A1" t="inlineStr"><is><t>%s</t></is></c>
                        </row>
                      </sheetData>
                    </worksheet>
                    """.formatted(escapeXml(testCase)));
        }
    }

    private void writeZipEntry(ZipOutputStream zip, String name, String content) throws Exception {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(content.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private String createSafeExcelSheetName(String value) {
        String sheetName = value.trim()
                .replaceAll("[\\\\/?*\\[\\]:\\p{Cntrl}]", "_")
                .replaceAll("^'+|'+$", "");
        if (sheetName.isBlank()) {
            sheetName = "TestCase";
        }
        if (sheetName.length() > 31) {
            sheetName = sheetName.substring(0, 31);
        }
        return sheetName;
    }

    private String escapeXml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String sanitizeWorkbookFileName(String value) {
        String fileName = value.trim()
                .replaceAll("[<>:\"/\\\\|?*\\p{Cntrl}]", "_")
                .replaceAll("[. ]+$", "");
        if (fileName.isBlank()) {
            fileName = "TestSuite";
        }
        if (fileName.length() > 120) {
            fileName = fileName.substring(0, 120).trim();
        }
        String upper = fileName.toUpperCase();
        if (upper.matches("CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9]")) {
            fileName = fileName + "_Workbook";
        }
        return fileName;
    }

    private JPanel createDbValidatorPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        root.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 16, 18, 16)
        ));

        JPanel content = new JPanel();
        content.setBackground(PANEL_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel topRow = new JPanel(new GridLayout(1, 2, 16, 0));
        topRow.setBackground(PANEL_BG);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.add(createDbConnectionCard());
        topRow.add(createDbQueryCard());
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));
        content.add(topRow);

        content.add(Box.createVerticalStrut(16));
        JPanel queryResultsCard = createDbQueryResultsCard();
        queryResultsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        queryResultsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        content.add(queryResultsCard);

        content.add(Box.createVerticalStrut(16));
        JPanel rulesCard = createDbRulesCard();
        rulesCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        rulesCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        content.add(rulesCard);

        content.add(Box.createVerticalStrut(16));
        JPanel resultsCard = createDbResultsCard();
        resultsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(resultsCard);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scrollPane, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel createDbConnectionCard() {
        JPanel card = new JPanel();
        card.setBackground(PANEL_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = createSectionTitle("Database Connection");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(10));

        dbTypeDropdown = new JComboBox<>(new String[]{"MySQL", "PostgreSQL", "Oracle", "SQL Server", "Custom"});
        jdbcUrlField = new JTextField("jdbc:mysql://localhost:3306/testdb");
        dbUsernameField = new JTextField("root");
        dbPasswordField = new JPasswordField();
        dbDefaultEchoChar = dbPasswordField.getEchoChar();
        driverClassField = new JTextField("com.mysql.cj.jdbc.Driver");

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(PANEL_BG);
        addDbField(fields, 0, "Database Type", dbTypeDropdown);
        addDbField(fields, 1, "JDBC URL", jdbcUrlField);
        addDbField(fields, 2, "Username", dbUsernameField);
        addDbField(fields, 3, "Password", wrapWithTrailingButton(dbPasswordField, createDbPasswordToggleButton()));
        addDbField(fields, 4, "Driver Class", driverClassField);
        fields.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(fields);

        card.add(Box.createVerticalStrut(14));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setBackground(PANEL_BG);
        testDbConnectionButton = createSecondaryButton("Test Connection");
        JButton saveConnectionButton = createSecondaryButton("Save Connection");
        JButton loadConnectionButton = createSecondaryButton("Load Connection");
        testDbConnectionButton.addActionListener(e -> testDbConnection());
        saveConnectionButton.addActionListener(e -> saveDbConnection());
        loadConnectionButton.addActionListener(e -> loadDbConnection());
        dbConnectionStatusLabel = createMetricValue("Not connected", new Color(95, 103, 120));
        actions.add(testDbConnectionButton);
        actions.add(saveConnectionButton);
        actions.add(loadConnectionButton);
        actions.add(dbConnectionStatusLabel);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(actions);

        dbTypeDropdown.addActionListener(e -> applyDbTypeDefaults());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return card;
    }

    private JPanel createDbQueryCard() {
        JPanel card = new JPanel();
        card.setBackground(PANEL_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = createSectionTitle("SQL Query");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(10));

        dbQueryArea = createEditorArea("SELECT id, name, email, updated_at\nFROM users\nWHERE id = ${id}");
        JScrollPane queryScroll = createLineNumberScrollPane(dbQueryArea);
        queryScroll.setPreferredSize(new Dimension(640, 220));
        queryScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
        card.add(queryScroll);

        card.add(Box.createVerticalStrut(10));
        JPanel footer = new JPanel(new BorderLayout(12, 0));
        footer.setBackground(PANEL_BG);
        JLabel hint = new JLabel("Use ${fieldName} from the latest API response body.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(new Color(95, 103, 120));
        JPanel queryActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        queryActions.setBackground(PANEL_BG);
        dbVariableDropdown = createVariableDropdown();
        JButton insertDbVariableBtn = createSecondaryButton("Insert Variable");
        JButton saveQueryBtn = createSecondaryButton("Save Query");
        JButton loadQueryBtn = createSecondaryButton("Load Query");
        JButton fillExampleBtn = createSecondaryButton("Use API Response Variables");
        insertDbVariableBtn.addActionListener(e -> insertSelectedVariable(dbQueryArea, dbVariableDropdown));
        saveQueryBtn.addActionListener(e -> saveDbQuery());
        loadQueryBtn.addActionListener(e -> loadDbQuery());
        fillExampleBtn.addActionListener(e -> populateDefaultDbRules());
        queryActions.add(dbVariableDropdown);
        queryActions.add(insertDbVariableBtn);
        queryActions.add(saveQueryBtn);
        queryActions.add(loadQueryBtn);
        queryActions.add(fillExampleBtn);
        footer.add(hint, BorderLayout.CENTER);
        footer.add(queryActions, BorderLayout.EAST);
        card.add(footer);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return card;
    }

    private JPanel createDbQueryResultsCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(PANEL_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.add(createSectionTitle("Query Resultset"), BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(PANEL_BG);
        runDbQueryButton = createSecondaryButton("Run Query");
        saveSelectedDbCellButton = createSecondaryButton("Save Selected Cell as Variable");
        runDbQueryButton.addActionListener(e -> runDbQuery());
        saveSelectedDbCellButton.addActionListener(e -> saveSelectedDbResultCellAsVariable());
        actions.add(runDbQueryButton);
        actions.add(saveSelectedDbCellButton);
        header.add(actions, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        dbQueryResultsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dbQueryResultsTable = new JTable(dbQueryResultsTableModel);
        dbQueryResultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dbQueryResultsTable.setRowHeight(26);
        dbQueryResultsTable.setCellSelectionEnabled(true);
        dbQueryResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dbQueryResultsTable.setFillsViewportHeight(true);
        renderDbQueryRows(List.of());
        card.add(new JScrollPane(dbQueryResultsTable), BorderLayout.CENTER);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return card;
    }

    private JPanel createDbRulesCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(PANEL_BG);

        dbValidationTestSuiteField = new JTextField();
        dbValidationTestCaseField = new JTextField();
        dbValidationTestStepField = new JTextField();

        JLabel title = createSectionTitle("Validation Rules (Compare API Response with DB Result)");
        JPanel header = new JPanel(new BorderLayout(0, 12));
        header.setBackground(PANEL_BG);
        header.add(title, BorderLayout.NORTH);
        header.add(createTestRunnerContextPanel(dbValidationTestSuiteField, dbValidationTestCaseField,
                dbValidationTestStepField, this::addDbValidationsToTestRunner), BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        JTabbedPane validationTabs = new JTabbedPane();
        validationTabs.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        validationTabs.setBackground(PANEL_BG);
        validationTabs.addTab("API-DB Validation", createApiDbValidationRulesPanel());
        validationTabs.addTab("DB Validation", createDbValidationPlaceholderPanel());
        card.add(validationTabs, BorderLayout.CENTER);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return card;
    }

    private JPanel createApiDbValidationRulesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));

        dbRulesTableModel = new DefaultTableModel(
                new String[]{"Validate", "API Field", "DB Column", "Operator", "Description"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };
        dbRulesTable = new JTable(dbRulesTableModel);
        dbRulesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dbRulesTable.setRowHeight(26);
        dbRulesTable.putClientProperty("terminateEditOnFocusLost", true);
        dbRulesTable.getColumnModel().getColumn(0).setMaxWidth(90);
        dbRulesTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(createVariableDropdown()));
        dbRulesTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(createDbColumnDropdown()));
        dbRulesTable.getColumnModel().getColumn(3).setCellEditor(
                new DefaultCellEditor(new JComboBox<>(new String[]{"=", "!=", "contains", ">", ">=", "<", "<="}))
        );
        populateDefaultDbRules();
        panel.add(new JScrollPane(dbRulesTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new BorderLayout());
        actions.setBackground(PANEL_BG);
        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftActions.setBackground(PANEL_BG);
        JButton addRuleBtn = createSecondaryButton("Add Rule");
        JButton removeRuleBtn = createSecondaryButton("Remove Rule");
        JButton checkAllBtn = createSecondaryButton("Check All");
        JButton uncheckAllBtn = createSecondaryButton("Un-Check All");
        JButton loadColumnsBtn = createSecondaryButton("Load DB Columns");
        JButton saveRulesBtn = createSecondaryButton("Save Rules");
        JButton loadRulesBtn = createSecondaryButton("Load Rules");
        addRuleBtn.addActionListener(e -> dbRulesTableModel.addRow(new Object[]{Boolean.TRUE, "", "", "=", ""}));
        removeRuleBtn.addActionListener(e -> removeSelectedDbRule());
        checkAllBtn.addActionListener(e -> setAllRowsChecked(dbRulesTable, dbRulesTableModel, true));
        uncheckAllBtn.addActionListener(e -> setAllRowsChecked(dbRulesTable, dbRulesTableModel, false));
        loadColumnsBtn.addActionListener(e -> loadDbColumnOptions());
        saveRulesBtn.addActionListener(e -> saveDbRules());
        loadRulesBtn.addActionListener(e -> loadDbRules());
        leftActions.add(addRuleBtn);
        leftActions.add(removeRuleBtn);
        leftActions.add(checkAllBtn);
        leftActions.add(uncheckAllBtn);
        leftActions.add(loadColumnsBtn);
        leftActions.add(saveRulesBtn);
        leftActions.add(loadRulesBtn);

        runDbValidationButton = createPrimaryButton("Run DB Validation");
        runDbValidationButton.addActionListener(e -> runDbValidation());
        actions.add(leftActions, BorderLayout.WEST);
        actions.add(runDbValidationButton, BorderLayout.EAST);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDbValidationPlaceholderPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(PANEL_BG);
        dbColumnValidationsStatusLabel = createMetricValue("Run a DB query to list columns for validation.", new Color(95, 103, 120));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(PANEL_BG);
        JButton resetBtn = createSecondaryButton("Reset Defaults");
        JButton checkAllBtn = createSecondaryButton("Check All");
        JButton uncheckAllBtn = createSecondaryButton("Un-Check All");
        JButton validateBtn = createPrimaryButton("Validate DB Columns");
        resetBtn.addActionListener(e -> resetDbColumnValidationDefaults());
        checkAllBtn.addActionListener(e -> setAllRowsChecked(dbColumnValidationsTable, dbColumnValidationsTableModel, true));
        uncheckAllBtn.addActionListener(e -> setAllRowsChecked(dbColumnValidationsTable, dbColumnValidationsTableModel, false));
        validateBtn.addActionListener(e -> runDbColumnValidations());
        actions.add(resetBtn);
        actions.add(checkAllBtn);
        actions.add(uncheckAllBtn);
        actions.add(validateBtn);
        toolbar.add(dbColumnValidationsStatusLabel, BorderLayout.WEST);
        toolbar.add(actions, BorderLayout.EAST);

        dbColumnValidationsTableModel = new DefaultTableModel(
                new String[]{"Validate", "DB Column Name", "Value", "Null Validation", "Type Validation", "Expected Value / Variable", "Result", "Actual Value", "Actual Type"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 3 || column == 4 || column == 5;
            }
        };
        dbColumnValidationsTable = new JTable(dbColumnValidationsTableModel);
        dbColumnValidationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dbColumnValidationsTable.setRowHeight(26);
        dbColumnValidationsTable.putClientProperty("terminateEditOnFocusLost", true);
        dbColumnValidationsTable.getColumnModel().getColumn(0).setMaxWidth(90);
        dbColumnValidationsTable.getColumnModel().getColumn(3).setCellEditor(
                new DefaultCellEditor(new JComboBox<>(dbNullValidationOptions()))
        );
        dbColumnValidationsTable.getColumnModel().getColumn(4).setCellEditor(
                new DefaultCellEditor(new JComboBox<>(dbTypeValidationOptions()))
        );
        dbColumnValidationsTable.getColumnModel().getColumn(6).setPreferredWidth(170);
        hideTableColumn(dbColumnValidationsTable, 7);
        hideTableColumn(dbColumnValidationsTable, 8);
        applyDbColumnValidationExpectedValueEditor();

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(dbColumnValidationsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDbResultsCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(PANEL_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.add(createSectionTitle("Validation Results"), BorderLayout.WEST);
        JPanel summary = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        summary.setBackground(PANEL_BG);
        dbSummaryLabel = createMetricValue("Total: 0", PRIMARY);
        dbPassedLabel = createMetricValue("Passed: 0", SUCCESS);
        dbFailedLabel = createMetricValue("Failed: 0", new Color(196, 70, 54));
        summary.add(dbSummaryLabel);
        summary.add(dbPassedLabel);
        summary.add(dbFailedLabel);
        header.add(summary, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        dbResultsTableModel = new DefaultTableModel(
                new String[]{"Field", "Expected (API)", "Actual (DB)", "Operator", "Status", "Message"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable resultsTable = new JTable(dbResultsTableModel);
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultsTable.setRowHeight(26);
        card.add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return card;
    }

    private JPanel createWebTestingPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        root.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 16, 18, 16)
        ));

        JPanel content = new JPanel();
        content.setBackground(PANEL_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(createWebTestingHeaderCard());
        content.add(Box.createVerticalStrut(16));
        content.add(createWebTestingStepsCard());
        content.add(Box.createVerticalStrut(16));
        content.add(createWebTestingRunnerCard());
        content.add(Box.createVerticalStrut(16));
        content.add(createWebTestingExecutionCard());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        card.add(scrollPane, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel createWebTestingHeaderCard() {
        JPanel card = new JPanel();
        card.setBackground(PANEL_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));

        JLabel title = createSectionTitle("Web Testing");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_BG);
        webTestNameField = new JTextField("Login Flow Test");
        webStartUrlField = new JTextField("https://demo.playwright.dev/todomvc");
        webCdpEndpointField = new JTextField("http://127.0.0.1:9222");
        addWebField(form, 0, 0, "Test Name", webTestNameField, 0.35, new Insets(0, 0, 12, 16));
        addWebField(form, 1, 0, "Start URL", webStartUrlField, 0.65, new Insets(0, 0, 12, 0));
        addWebField(form, 0, 1, "Active Browser CDP URL", webCdpEndpointField, 1.0,
                new Insets(0, 0, 12, 0), 2);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
        card.add(form);

        JPanel toolbar = new JPanel();
        toolbar.setBackground(PANEL_BG);
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 172));

        JPanel actionRow = new JPanel();
        actionRow.setBackground(PANEL_BG);
        actionRow.setLayout(new BoxLayout(actionRow, BoxLayout.X_AXIS));
        webRecordButton = createPrimaryButton("Record");
        webAttachButton = createSecondaryButton("Attach Active Browser");
        webLaunchDebugChromeButton = createSecondaryButton("Launch Debug Chrome");
        webStopButton = createSecondaryButton("Stop");
        webStopNoBrowserCloseButton = createSecondaryButton("Stop-No Browser Close");
        JButton clearButton = createSecondaryButton("Clear Steps");
        JButton saveButton = createSecondaryButton("Save Recording");
        JButton loadButton = createSecondaryButton("Load Recording");
        JButton screenshotButton = createSecondaryButton("Add Screenshot Step");
        webRecordButton.addActionListener(e -> startWebRecording());
        webAttachButton.addActionListener(e -> startAttachedWebRecording());
        webLaunchDebugChromeButton.addActionListener(e -> launchDebugChrome());
        webStopButton.addActionListener(e -> stopWebRecording());
        webStopNoBrowserCloseButton.addActionListener(e -> stopWebRecordingWithoutClosingBrowser());
        clearButton.addActionListener(e -> clearWebSteps());
        saveButton.addActionListener(e -> saveWebRecording());
        loadButton.addActionListener(e -> loadWebRecording());
        screenshotButton.addActionListener(e -> addWebScreenshotStep());
        webStopButton.setEnabled(false);
        webStopNoBrowserCloseButton.setEnabled(false);
        actionRow.add(webRecordButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(webAttachButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(webLaunchDebugChromeButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(webStopButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(webStopNoBrowserCloseButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(clearButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(saveButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(loadButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(screenshotButton);

        JPanel actionScrollContent = new JPanel(new BorderLayout());
        actionScrollContent.setBackground(PANEL_BG);
        actionScrollContent.setBorder(new EmptyBorder(0, 0, 28, 0));
        actionScrollContent.add(actionRow, BorderLayout.NORTH);

        JScrollPane actionScroll = new JScrollPane(actionScrollContent);
        actionScroll.setBorder(BorderFactory.createEmptyBorder());
        actionScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        actionScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        actionScroll.getViewport().setBackground(PANEL_BG);
        actionScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
        actionScroll.setPreferredSize(new Dimension(1000, 96));

        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(PANEL_BG);
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        webRecorderStatusLabel = createMetricValue("Recorder idle", new Color(95, 103, 120));
        webBrowserUrlLabel = createMetricValue("Browser URL: not started", PRIMARY);
        webSelectorHintLabel = createMetricValue("Selector priority: data-testid -> id -> name -> aria -> CSS -> fallback path", new Color(95, 103, 120));
        webRecorderStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        webBrowserUrlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        webSelectorHintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusPanel.add(webRecorderStatusLabel);
        statusPanel.add(Box.createVerticalStrut(4));
        statusPanel.add(webBrowserUrlLabel);
        statusPanel.add(Box.createVerticalStrut(4));
        statusPanel.add(webSelectorHintLabel);

        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.add(actionScroll);
        toolbar.add(Box.createVerticalStrut(8));
        toolbar.add(statusPanel);
        card.add(toolbar);
        card.add(Box.createVerticalStrut(12));

        webTipsArea = createResponseArea();
        webTipsArea.setRows(4);
        webTipsArea.setLineWrap(true);
        webTipsArea.setWrapStyleWord(true);
        webTipsArea.setText("Click Record for a fresh browser, or Attach Active Browser for an open Chromium browser started with remote debugging such as --remote-debugging-port=9222. Use http://127.0.0.1:9222 for the CDP URL.");
        webTipsArea.setBackground(new Color(255, 250, 235));
        webTipsArea.setCaretPosition(0);
        JScrollPane tipScroll = new JScrollPane(webTipsArea);
        tipScroll.setBorder(BorderFactory.createLineBorder(new Color(243, 206, 96)));
        tipScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tipScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tipScroll.setPreferredSize(new Dimension(1000, 118));
        tipScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 126));
        tipScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(tipScroll);
        return card;
    }

    private JPanel createWebTestingStepsCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 430));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.add(createSectionTitle("Captured Steps"), BorderLayout.WEST);
        header.add(createMetricValue("Recording uses Playwright + browser-side event listener.", new Color(95, 103, 120)),
                BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        webStepsTableModel = new DefaultTableModel(
                new String[]{"#", "Action", "Selector", "Value / Expected", "Notes"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        webStepsTable = new JTable(webStepsTableModel);
        webStepsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        webStepsTable.setRowHeight(28);
        card.add(new JScrollPane(webStepsTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setBackground(PANEL_BG);
        webVariableDropdown = createVariableDropdown();
        JButton insertWebVariableButton = createSecondaryButton("Insert Variable");
        JButton addButton = createSecondaryButton("Add Step");
        JButton editButton = createSecondaryButton("Edit Step");
        JButton deleteButton = createSecondaryButton("Delete");
        JButton moveUpButton = createSecondaryButton("Move Up");
        JButton moveDownButton = createSecondaryButton("Move Down");
        JButton mergeButton = createSecondaryButton("Merge Recording");
        insertWebVariableButton.addActionListener(e -> insertSelectedVariableIntoWebStep());
        addButton.addActionListener(e -> addWebStep());
        editButton.addActionListener(e -> editSelectedWebStep());
        deleteButton.addActionListener(e -> deleteSelectedWebStep());
        moveUpButton.addActionListener(e -> moveSelectedWebStep(-1));
        moveDownButton.addActionListener(e -> moveSelectedWebStep(1));
        mergeButton.addActionListener(e -> mergeWebRecording());
        actions.add(webVariableDropdown);
        actions.add(insertWebVariableButton);
        actions.add(addButton);
        actions.add(editButton);
        actions.add(deleteButton);
        actions.add(moveUpButton);
        actions.add(moveDownButton);
        actions.add(mergeButton);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createWebTestingRunnerCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        JPanel header = new JPanel();
        header.setBackground(PANEL_BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = createSectionTitle("Add Captured Test Steps to Test Runner");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel hint = createMetricValue("Uses the imported Test Suite Runner workbook and keeps web step variables for runtime.",
                new Color(95, 103, 120));
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(hint);
        card.add(header, BorderLayout.NORTH);

        webTestingTestSuiteField = new JTextField();
        webTestingTestCaseField = new JTextField();
        webTestingTestStepField = new JTextField();
        JPanel runnerContext = createTestRunnerContextPanel(webTestingTestSuiteField, webTestingTestCaseField,
                webTestingTestStepField, this::addWebTestToTestRunner);
        runnerContext.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(runnerContext, BorderLayout.CENTER);
        return card;
    }

    private JPanel createWebTestingExecutionCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 520));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.add(createSectionTitle("Execution"), BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        controls.setBackground(PANEL_BG);
        webRunButton = createPrimaryButton("Run Web Test");
        webStopRunButton = createSecondaryButton("Stop Web Test");
        webRetestButton = createSecondaryButton("Re-Test Selected");
        webHeadlessCheckbox = new JCheckBox("Headless");
        webHeadlessCheckbox.setBackground(PANEL_BG);
        webHeadlessCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        webSlowMoCheckbox = new JCheckBox("Slow Motion (250ms)");
        webSlowMoCheckbox.setBackground(PANEL_BG);
        webSlowMoCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        webRunButton.addActionListener(e -> runWebTest());
        webStopRunButton.addActionListener(e -> stopWebTest());
        webRetestButton.addActionListener(e -> retestSelectedWebSteps());
        webStopRunButton.setEnabled(false);
        controls.add(webRunButton);
        controls.add(webStopRunButton);
        controls.add(webRetestButton);
        controls.add(webHeadlessCheckbox);
        controls.add(webSlowMoCheckbox);
        header.add(controls, BorderLayout.CENTER);

        JPanel summary = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        summary.setBackground(PANEL_BG);
        webRunSummaryLabel = createMetricValue("Total: 0", PRIMARY);
        webRunPassedLabel = createMetricValue("Passed: 0", SUCCESS);
        webRunFailedLabel = createMetricValue("Failed: 0", new Color(196, 70, 54));
        summary.add(webRunSummaryLabel);
        summary.add(webRunPassedLabel);
        summary.add(webRunFailedLabel);
        header.add(summary, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 16, 0));
        body.setBackground(PANEL_BG);

        webResultsTableModel = new DefaultTableModel(
                new String[]{"#", "Action", "Selector", "Expected / Value", "Status", "Message", "Duration"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3;
            }
        };
        webResultsTable = new JTable(webResultsTableModel);
        webResultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        webResultsTable.setRowHeight(26);
        webResultsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        webResultsTable.setCellSelectionEnabled(true);
        webResultsTable.putClientProperty("terminateEditOnFocusLost", true);
        configureWebResultsEditing();
        configureWebResultsCopying();
        body.add(wrapCard("Step Results", new JScrollPane(webResultsTable)));

        JPanel notesPanel = new JPanel();
        notesPanel.setBackground(PANEL_BG);
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        webScreenshotLabel = createMetricValue("Last screenshot: not captured yet", new Color(95, 103, 120));
        webScreenshotLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextArea notes = createResponseArea();
        notes.setText("Smart behavior:\n- selector generation prefers stable attributes over XPath\n- navigations are added automatically\n- validation suggestions are proposed after page transitions\n- screenshot steps can be added manually before execution");
        notes.setRows(9);
        JScrollPane notesScroll = new JScrollPane(notes);
        notesScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        notesPanel.add(webScreenshotLabel);
        notesPanel.add(Box.createVerticalStrut(12));
        notesPanel.add(notesScroll);
        body.add(wrapCard("Recorder Notes", notesPanel));

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createPerformancePanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        root.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 16, 18, 16)
        ));

        JPanel content = new JPanel();
        content.setBackground(PANEL_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = createSectionTitle("Performance Test");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(8));

        performanceTestSuiteField = new JTextField();
        performanceTestCaseField = new JTextField();
        performanceTestStepField = new JTextField();
        JPanel runnerContext = createTestRunnerContextPanel(performanceTestSuiteField, performanceTestCaseField,
                performanceTestStepField, this::addPerformanceTestToTestRunner);
        runnerContext.setAlignmentX(Component.LEFT_ALIGNMENT);
        runnerContext.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));
        content.add(runnerContext);
        content.add(Box.createVerticalStrut(12));

        performanceSourceLabel = new JLabel("Source Request: Method and endpoint will be taken from API Tester at runtime.");
        performanceSourceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        performanceSourceLabel.setForeground(new Color(60, 66, 79));
        performanceSourceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(performanceSourceLabel);

        performanceConfigLabel = new JLabel("Plan: configure threads and iterations below. Request body is applied for POST, PUT, and PATCH.");
        performanceConfigLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        performanceConfigLabel.setForeground(new Color(95, 103, 120));
        performanceConfigLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(4));
        content.add(performanceConfigLabel);

        content.add(Box.createVerticalStrut(16));
        JPanel configRow = new JPanel(new GridBagLayout());
        configRow.setBackground(PANEL_BG);
        configRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        performanceThreadsSpinner = createSpinner(10, 1, 1000, 1);
        performanceIterationsSpinner = createSpinner(100, 1, 100000, 1);
        addPerformanceConfigField(configRow, 0, "Threads:", performanceThreadsSpinner);
        addPerformanceConfigField(configRow, 2, "Iterations Per Thread:", performanceIterationsSpinner);
        configRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        content.add(configRow);

        content.add(Box.createVerticalStrut(16));
        performanceBodyArea = createEditorArea("");
        performanceBodyArea.setRows(3);
        JScrollPane performanceBodyScroll = createLineNumberScrollPane(performanceBodyArea);
        performanceBodyScroll.setPreferredSize(new Dimension(900, 96));
        performanceBodyScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        JPanel performanceBodyFooter = new JPanel(new BorderLayout(12, 0));
        performanceBodyFooter.setBackground(PANEL_BG);
        JLabel randomHint = new JLabel("Use {$randomstring} and {$randomint} for runtime-generated values");
        randomHint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        randomHint.setForeground(new Color(95, 103, 120));
        JButton copyApiBodyBtn = createSecondaryButton("Copy From API Tester");
        copyApiBodyBtn.addActionListener(e -> performanceBodyArea.setText(bodyArea.getText()));
        performanceBodyFooter.add(randomHint, BorderLayout.CENTER);
        performanceBodyFooter.add(copyApiBodyBtn, BorderLayout.EAST);
        JPanel performanceBodyCard = createTrackedPerformanceBodyCard(
                "Performance Request Body (Optional)", performanceBodyScroll, performanceBodyFooter
        );
        performanceBodyCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        performanceBodyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 162));
        content.add(performanceBodyCard);

        content.add(Box.createVerticalStrut(16));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setBackground(PANEL_BG);
        runLoadTestButton = createPrimaryButton("Run Load Test");
        openReportButton = createSecondaryButton("Open HTML Report");
        openReportButton.setEnabled(false);
        runLoadTestButton.addActionListener(e -> runPerformanceTest());
        openReportButton.addActionListener(e -> openPerformanceReport());
        actions.add(runLoadTestButton);
        actions.add(openReportButton);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        content.add(actions);

        content.add(Box.createVerticalStrut(16));
        JPanel metricsGrid = new JPanel(new GridLayout(1, 3, 14, 0));
        metricsGrid.setBackground(PANEL_BG);
        metricsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        performanceSamplesLabel = createMetricValue("Samples: --", PRIMARY);
        performanceErrorsLabel = createMetricValue("Errors: --", new Color(196, 70, 54));
        performanceThroughputLabel = createMetricValue("Throughput: --", SUCCESS);
        performanceDurationLabel = createMetricValue("Duration: --", PRIMARY);
        performanceReportLabel = createMetricValue("Report: not generated", new Color(95, 103, 120));
        JLabel noteLabel = createMetricValue("Result: awaiting execution", new Color(95, 103, 120));
        metricsGrid.add(createMetricCard("Samples & Errors", performanceSamplesLabel, performanceErrorsLabel));
        metricsGrid.add(createMetricCard("Speed", performanceThroughputLabel, performanceDurationLabel));
        metricsGrid.add(createMetricCard("Report", performanceReportLabel, noteLabel));
        metricsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 104));
        content.add(metricsGrid);

        performanceChartPanel = new PerformanceChartPanel();
        performanceLogArea = createResponseArea();
        performanceLogArea.setRows(10);
        performanceLogArea.setText("Load test results will appear here after execution.");

        content.add(Box.createVerticalStrut(16));
        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setBackground(PANEL_BG);
        center.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(wrapCard("Latency Graph", performanceChartPanel));
        center.add(wrapCard("Execution Log", new JScrollPane(performanceLogArea)));
        center.setPreferredSize(new Dimension(1100, 320));
        center.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        content.add(center);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        card.add(scrollPane, BorderLayout.CENTER);
        updatePerformanceBodyState();

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private void sendRequest() {
        String url;
        try {
            url = normalizeEndpointUrl(resolveRuntimeVariablesInText(resolveVariablesInText(endpointField.getText().trim())));
        } catch (IllegalArgumentException e) {
            renderError(e.getMessage());
            return;
        }
        if (url.isEmpty()) {
            renderError("Endpoint is required.");
            return;
        }
        if (!url.equals(endpointField.getText().trim())) {
            endpointField.setText(url);
        }

        ApiRequest req = new ApiRequest();
        req.url = url;
        req.method = methodDropdown.getSelectedItem().toString();
        req.body = isBodyAllowedForSelectedMethod()
                ? resolveRuntimeVariablesInText(resolveVariablesInText(bodyArea.getText().trim()))
                : "";
        req.headers = parseHeaders(resolveRuntimeVariablesInText(resolveVariablesInText(headersArea.getText())));
        req.token = authTypeDropdown.getSelectedItem().equals("Bearer Token")
                ? new String(tokenField.getPassword()).trim()
                : "";
        req.token = resolveRuntimeVariablesInText(resolveVariablesInText(req.token));

        setRequestInProgress(true);
        statusValueLabel.setText("Sending...");
        statusValueLabel.setForeground(PRIMARY);
        timeValueLabel.setText("--");
        sizeValueLabel.setText("--");
        prettyResponseArea.setText("Sending request...");
        rawResponseArea.setText("Sending request...");
        responseHeadersArea.setText("");
        responseCookiesArea.setText("");

        SwingWorker<ApiResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected ApiResponse doInBackground() {
                return apiService.sendRequest(req);
            }

            @Override
            protected void done() {
                setRequestInProgress(false);
                try {
                    renderResponse(get());
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    renderError(cause.getMessage() != null ? cause.getMessage() : "Request failed.");
                }
            }
        };
        worker.execute();
    }

    private String normalizeEndpointUrl(String endpoint) {
        if (endpoint == null) {
            return "";
        }
        String normalized = repairEmptyHostPortUrl(
                removeAccidentalJsonFromEndpoint(endpoint.trim()).replaceAll("\\s+", "")
        );
        if (containsRawPathTemplate(normalized)) {
            throw new IllegalArgumentException("Endpoint contains raw {...} text. Keep JSON payload in Request Body only, not in Endpoint.");
        }
        return normalized;
    }

    private String repairEmptyHostPortUrl(String endpoint) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("^(https?://):\\d+/((?:\\d{1,3}\\.){3}\\d{1,3}|[A-Za-z0-9.-]+)(/.*)?$")
                .matcher(endpoint);
        if (matcher.matches()) {
            return matcher.group(1) + matcher.group(2) + (matcher.group(3) == null ? "" : matcher.group(3));
        }
        return endpoint;
    }

    private String removeAccidentalJsonFromEndpoint(String endpoint) {
        StringBuilder cleaned = new StringBuilder();
        int index = 0;
        while (index < endpoint.length()) {
            char current = endpoint.charAt(index);
            if (current != '{') {
                cleaned.append(current);
                index++;
                continue;
            }

            int end = findMatchingBrace(endpoint, index);
            if (end < 0) {
                cleaned.append(current);
                index++;
                continue;
            }

            String block = endpoint.substring(index, end + 1);
            if (looksLikeJsonObject(block)) {
                index = end + 1;
            } else {
                cleaned.append(block);
                index = end + 1;
            }
        }
        return cleaned.toString().trim();
    }

    private int findMatchingBrace(String text, int start) {
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;
        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);
            if (escaped) {
                escaped = false;
                continue;
            }
            if (c == '\\' && inString) {
                escaped = true;
                continue;
            }
            if (c == '"') {
                inString = !inString;
                continue;
            }
            if (inString) {
                continue;
            }
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean looksLikeJsonObject(String text) {
        return text.contains(":") && (text.contains("\"") || text.contains("'"));
    }

    private boolean containsRawPathTemplate(String endpoint) {
        return endpoint.contains("{") || endpoint.contains("}");
    }

    private void runPerformanceTest() {
        ApiRequest request = buildCurrentRequest();
        if (request.url == null || request.url.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter an endpoint in API Tester before running a performance test.",
                    "Performance Test", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int threads = ((Number) performanceThreadsSpinner.getValue()).intValue();
        int iterations = ((Number) performanceIterationsSpinner.getValue()).intValue();

        performanceSourceLabel.setText("Source Request: " + request.method + " " + request.url);
        performanceConfigLabel.setText("Plan: " + threads + " threads x " + iterations + " iterations each. "
                + ("POST".equals(request.method) || "PUT".equals(request.method) || "PATCH".equals(request.method)
                ? "Request body will be included." : "Request body will not be sent."));
        performanceLogArea.setText("Preparing load test...\n");
        performanceSamplesLabel.setText("Samples: running...");
        performanceErrorsLabel.setText("Errors: --");
        performanceThroughputLabel.setText("Throughput: --");
        performanceDurationLabel.setText("Duration: --");
        performanceReportLabel.setText("Report: generating...");
        performanceChartPanel.updateValues(Map.of());
        runLoadTestButton.setEnabled(false);
        openReportButton.setEnabled(false);

        SwingWorker<PerformanceTestResult, String> worker = new SwingWorker<>() {
            @Override
            protected PerformanceTestResult doInBackground() throws Exception {
                publish("Using API Tester request configuration.");
                publish("Executing " + threads + " threads with " + iterations + " iterations each using jmeter-java-dsl...");
                PerformanceTestResult result = performanceTestService.runLoadTest(request, threads, iterations);
                publish("Load test completed. HTML report generated at: " + result.reportIndexPath);
                return result;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    appendPerformanceLog(chunk);
                }
            }

            @Override
            protected void done() {
                runLoadTestButton.setEnabled(true);
                try {
                    PerformanceTestResult result = get();
                    renderPerformanceResult(result);
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    performanceReportLabel.setText("Report: failed");
                    appendPerformanceLog("Load test failed: " + cause.getMessage());
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            cause.getMessage(), "Performance Test Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void openPerformanceReport() {
        if (lastPerformanceReportPath == null || !Files.exists(lastPerformanceReportPath)) {
            JOptionPane.showMessageDialog(this, "No HTML report is available yet.",
                    "Performance Report", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(lastPerformanceReportPath.toUri());
            } else {
                JOptionPane.showMessageDialog(this,
                        "HTML report saved at:\n" + lastPerformanceReportPath.toAbsolutePath(),
                        "Performance Report", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Report saved at:\n" + lastPerformanceReportPath.toAbsolutePath(),
                    "Performance Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void runCompare() {
        runCompare(false);
    }

    private void runCompare(boolean showMatched) {
        try {
            if (showMatched && lastExpectedJson != null && lastActualJson != null) {
                List<Object[]> results = filterMatchedRows(
                        comparator.compare(lastExpectedJson, lastActualJson, lastStrictCompare, true)
                );
                tableModel.setRowCount(0);
                results.forEach(tableModel::addRow);
                return;
            }

            String filePath = filePathField.getText().trim();
            if (filePath.isEmpty()) {
                throw new IllegalArgumentException("Expected Response File path is required.");
            }
            String expected = Files.readString(Path.of(filePath));
            String actual = rawResponseArea.getText();
            if (actual == null || actual.isBlank() || "Sending request...".equals(actual)) {
                throw new IllegalArgumentException("Send a request in API Tester first so there is an actual response to compare.");
            }

            boolean strict = compareModeDropdown.getSelectedItem().equals("STRICT");
            lastExpectedJson = expected;
            lastActualJson = actual;
            lastStrictCompare = strict;

            List<Object[]> results = comparator.compare(expected, actual, strict, showMatched);
            if (showMatched) {
                results = filterMatchedRows(results);
            }
            tableModel.setRowCount(0);
            results.forEach(tableModel::addRow);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Compare Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void testDbConnection() {
        DbConnectionConfig config = buildDbConnectionConfig();
        testDbConnectionButton.setEnabled(false);
        dbConnectionStatusLabel.setText("Connecting...");
        dbConnectionStatusLabel.setForeground(PRIMARY);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                dbValidationService.testConnection(config);
                return null;
            }

            @Override
            protected void done() {
                testDbConnectionButton.setEnabled(true);
                try {
                    get();
                    dbConnectionStatusLabel.setText("Connected successfully");
                    dbConnectionStatusLabel.setForeground(SUCCESS);
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    dbConnectionStatusLabel.setText("Connection failed");
                    dbConnectionStatusLabel.setForeground(new Color(196, 70, 54));
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            cause.getMessage(), "DB Connection Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void saveDbConnection() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("db-connection.json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            DbConnectionConfig config = buildDbConnectionConfig();
            JSONObject json = new JSONObject();
            json.put("databaseType", config.databaseType);
            json.put("jdbcUrl", config.jdbcUrl);
            json.put("username", config.username);
            json.put("password", config.password);
            json.put("driverClass", config.driverClass);
            Files.writeString(chooser.getSelectedFile().toPath(), json.toString(2));
            dbConnectionFilePath = chooser.getSelectedFile().toPath().toAbsolutePath();
            dbConnectionStatusLabel.setText("Connection details saved");
            dbConnectionStatusLabel.setForeground(SUCCESS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Connection Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDbConnection() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            JSONObject json = new JSONObject(Files.readString(chooser.getSelectedFile().toPath()));
            DbConnectionConfig config = new DbConnectionConfig();
            config.databaseType = json.optString("databaseType", "MySQL");
            config.jdbcUrl = json.optString("jdbcUrl");
            config.username = json.optString("username");
            config.password = json.optString("password");
            config.driverClass = json.optString("driverClass");
            applyDbConnectionConfig(config);
            dbConnectionFilePath = chooser.getSelectedFile().toPath().toAbsolutePath();
            dbConnectionStatusLabel.setText("Connection details loaded");
            dbConnectionStatusLabel.setForeground(PRIMARY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Load Connection Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDbQuery() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("db-query.sql"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            Files.writeString(chooser.getSelectedFile().toPath(), dbQueryArea.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Query Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDbQuery() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            dbQueryArea.setText(Files.readString(chooser.getSelectedFile().toPath()));
            dbQueryArea.setCaretPosition(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Load Query Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runDbValidation() {
        stopDbRuleEditing();
        DbConnectionConfig config = buildDbConnectionConfig();
        List<DbValidationRule> rules = collectDbRules();
        String sqlQuery = dbQueryArea.getText();
        if (rules.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Check at least one API-DB validation rule before running DB validation.",
                    "DB Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        runDbValidationButton.setEnabled(false);
        if (runDbQueryButton != null) {
            runDbQueryButton.setEnabled(false);
        }
        dbResultsTableModel.setRowCount(0);
        renderDbQueryRows(List.of());
        dbSummaryLabel.setText("Total: running...");
        dbPassedLabel.setText("Passed: --");
        dbFailedLabel.setText("Failed: --");

        SwingWorker<DbValidationReport, Void> worker = new SwingWorker<>() {
            @Override
            protected DbValidationReport doInBackground() throws Exception {
                return dbValidationService.validate(config, sqlQuery, rules, lastApiResponseBody, snapshotSavedVariables());
            }

            @Override
            protected void done() {
                runDbValidationButton.setEnabled(true);
                if (runDbQueryButton != null) {
                    runDbQueryButton.setEnabled(true);
                }
                try {
                    DbValidationReport report = get();
                    renderDbValidationReport(report);
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    dbSummaryLabel.setText("Total: 0");
                    dbPassedLabel.setText("Passed: 0");
                    dbFailedLabel.setText("Failed: 0");
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            cause.getMessage(), "DB Validation Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void runDbQuery() {
        DbConnectionConfig config = buildDbConnectionConfig();
        String sqlQuery = dbQueryArea.getText();

        runDbQueryButton.setEnabled(false);
        if (runDbValidationButton != null) {
            runDbValidationButton.setEnabled(false);
        }
        renderDbQueryRows(List.of());
        dbSummaryLabel.setText("Total: running query...");

        SwingWorker<List<Map<String, Object>>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Map<String, Object>> doInBackground() throws Exception {
                return dbValidationService.executeQuery(config, sqlQuery, lastApiResponseBody, snapshotSavedVariables());
            }

            @Override
            protected void done() {
                runDbQueryButton.setEnabled(true);
                if (runDbValidationButton != null) {
                    runDbValidationButton.setEnabled(true);
                }
                try {
                    List<Map<String, Object>> rows = get();
                    renderDbQueryRows(rows);
                    updateDbColumnOptionsFromRows(rows);
                    dbSummaryLabel.setText("Total: query rows " + rows.size());
                    dbPassedLabel.setText("Passed: --");
                    dbFailedLabel.setText("Failed: --");
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    dbSummaryLabel.setText("Total: 0");
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            cause.getMessage(), "Run Query Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void renderDbValidationReport(DbValidationReport report) {
        dbResultsTableModel.setRowCount(0);
        for (DbValidationResult result : report.results) {
            dbResultsTableModel.addRow(new Object[]{
                    result.field,
                    result.expectedValue,
                    result.actualValue,
                    result.operator,
                    result.passed ? "PASS" : "FAIL",
                    result.message
            });
        }
        dbSummaryLabel.setText("Total: " + report.total);
        dbPassedLabel.setText("Passed: " + report.passed);
        dbFailedLabel.setText("Failed: " + report.failed);
        renderDbQueryRows(report.dbRows);
        updateDbColumnOptionsFromRows(report.dbRows);
    }

    private void renderDbQueryRows(List<Map<String, Object>> dbRows) {
        if (dbQueryResultsTableModel == null) {
            return;
        }

        dbQueryResultsTableModel.setRowCount(0);
        dbQueryResultsTableModel.setColumnCount(0);
        dbQueryResultsTableModel.addColumn("#");

        if (dbRows == null || dbRows.isEmpty()) {
            refreshDbColumnValidationRows(List.of());
            return;
        }

        List<String> columnLabels = new ArrayList<>(dbRows.get(0).keySet());
        for (String columnLabel : columnLabels) {
            dbQueryResultsTableModel.addColumn(columnLabel);
        }

        for (int rowIndex = 0; rowIndex < dbRows.size(); rowIndex++) {
            Map<String, Object> dbRow = dbRows.get(rowIndex);
            Object[] values = new Object[columnLabels.size() + 1];
            values[0] = rowIndex;
            for (int columnIndex = 0; columnIndex < columnLabels.size(); columnIndex++) {
                values[columnIndex + 1] = dbRow.get(columnLabels.get(columnIndex));
            }
            dbQueryResultsTableModel.addRow(values);
        }
        resizeDbQueryResultColumns();
        refreshDbColumnValidationRows(dbRows);
    }

    private void resizeDbQueryResultColumns() {
        if (dbQueryResultsTable == null || dbQueryResultsTableModel == null) {
            return;
        }

        FontMetrics metrics = dbQueryResultsTable.getFontMetrics(dbQueryResultsTable.getFont());
        for (int column = 0; column < dbQueryResultsTableModel.getColumnCount(); column++) {
            int width = metrics.stringWidth(dbQueryResultsTableModel.getColumnName(column)) + 32;
            for (int row = 0; row < dbQueryResultsTableModel.getRowCount(); row++) {
                Object value = dbQueryResultsTableModel.getValueAt(row, column);
                width = Math.max(width, metrics.stringWidth(value == null ? "" : String.valueOf(value)) + 32);
            }
            dbQueryResultsTable.getColumnModel().getColumn(column).setPreferredWidth(Math.min(Math.max(width, 90), 420));
        }
    }

    private void saveSelectedDbResultCellAsVariable() {
        if (dbQueryResultsTable == null || dbQueryResultsTableModel == null) {
            return;
        }

        int viewRow = dbQueryResultsTable.getSelectedRow();
        int viewColumn = dbQueryResultsTable.getSelectedColumn();
        if (viewRow < 0 || viewColumn < 0) {
            JOptionPane.showMessageDialog(this, "Select a resultset cell to save as a variable.",
                    "Save DB Variable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = dbQueryResultsTable.convertRowIndexToModel(viewRow);
        int modelColumn = dbQueryResultsTable.convertColumnIndexToModel(viewColumn);
        if (modelColumn == 0) {
            JOptionPane.showMessageDialog(this, "Select a data column cell, not the row number.",
                    "Save DB Variable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String columnName = dbQueryResultsTableModel.getColumnName(modelColumn);
        Object value = dbQueryResultsTableModel.getValueAt(modelRow, modelColumn);
        String defaultName = sanitizeVariableName("db_" + columnName + "_" + modelRow);
        String variableName = JOptionPane.showInputDialog(this,
                "Variable name for row " + modelRow + ", column " + columnName + ":",
                defaultName);
        variableName = normalizeVariableName(variableName);
        if (variableName.isBlank()) {
            return;
        }

        savedVariables.put(variableName, value == null ? "" : String.valueOf(value));
        savedVariablePaths.put(variableName, "db." + columnName + "[" + modelRow + "]");
        savedVariableTypes.put(variableName, dbValueType(value));
        refreshSavedVariablesView();
        JOptionPane.showMessageDialog(this, "Saved ${" + variableName + "} from DB resultset.");
    }

    private void refreshDbColumnValidationRows(List<Map<String, Object>> dbRows) {
        if (dbColumnValidationsTableModel == null) {
            return;
        }

        stopDbColumnValidationEditing();
        dbColumnValidationsTableModel.setRowCount(0);
        if (dbRows == null || dbRows.isEmpty()) {
            if (dbColumnValidationsStatusLabel != null) {
                dbColumnValidationsStatusLabel.setText("No DB resultset columns available.");
                dbColumnValidationsStatusLabel.setForeground(new Color(95, 103, 120));
            }
            return;
        }

        int rowCount = dbRows.size();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Map<String, Object> dbRow = dbRows.get(rowIndex);
            for (Map.Entry<String, Object> entry : dbRow.entrySet()) {
                Object value = entry.getValue();
                String columnLabel = rowCount > 1 ? entry.getKey() + "[" + rowIndex + "]" : entry.getKey();
                dbColumnValidationsTableModel.addRow(new Object[]{
                        Boolean.TRUE,
                        columnLabel,
                        previewDbValue(value),
                        "Not Null",
                        dbValueType(value),
                        "",
                        "Not run",
                        value == null ? "" : String.valueOf(value),
                        dbValueType(value)
                });
            }
        }

        if (dbColumnValidationsStatusLabel != null) {
            dbColumnValidationsStatusLabel.setText("Configure null, type, and expected-value checks for DB columns.");
            dbColumnValidationsStatusLabel.setForeground(SUCCESS);
        }
    }

    private String previewDbValue(Object value) {
        if (value == null) {
            return "null";
        }
        String text = String.valueOf(value);
        return text.length() > 140 ? text.substring(0, 137) + "..." : text;
    }

    private String[] dbNullValidationOptions() {
        return new String[]{"Not Null", "Null", "Not Empty", "Empty", "Not Blank", "Blank", "Skip"};
    }

    private String[] dbTypeValidationOptions() {
        return new String[]{
                "Skip", "string", "number", "integer", "decimal", "boolean", "date", "time",
                "datetime", "timestamp", "uuid", "json", "binary", "null"
        };
    }

    private void resetDbColumnValidationDefaults() {
        if (dbColumnValidationsTableModel == null) {
            return;
        }
        stopDbColumnValidationEditing();
        for (int row = 0; row < dbColumnValidationsTableModel.getRowCount(); row++) {
            String type = stringCellValue(dbColumnValidationsTableModel, row, 8);
            dbColumnValidationsTableModel.setValueAt("Not Null", row, 3);
            dbColumnValidationsTableModel.setValueAt(type.isBlank() ? "Skip" : type, row, 4);
            dbColumnValidationsTableModel.setValueAt("", row, 5);
            dbColumnValidationsTableModel.setValueAt("Not run", row, 6);
        }
        if (dbColumnValidationsStatusLabel != null) {
            dbColumnValidationsStatusLabel.setText("DB column validation defaults restored.");
            dbColumnValidationsStatusLabel.setForeground(new Color(95, 103, 120));
        }
    }

    private void runDbColumnValidations() {
        if (dbColumnValidationsTableModel == null || dbColumnValidationsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No DB columns are available to validate. Run a query first.",
                    "DB Column Validations", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stopDbColumnValidationEditing();
        int passed = 0;
        int failed = 0;
        int selected = 0;
        for (int row = 0; row < dbColumnValidationsTableModel.getRowCount(); row++) {
            if (!isRowChecked(dbColumnValidationsTableModel, row)) {
                dbColumnValidationsTableModel.setValueAt("Skipped", row, 6);
                continue;
            }
            selected++;

            String actualType = stringCellValue(dbColumnValidationsTableModel, row, 8);
            String actualValue = stringCellValue(dbColumnValidationsTableModel, row, 7);
            String nullRule = stringCellValue(dbColumnValidationsTableModel, row, 3);
            String typeRule = stringCellValue(dbColumnValidationsTableModel, row, 4);
            String expectedValue = stringCellValue(dbColumnValidationsTableModel, row, 5);

            List<String> errors = new ArrayList<>();
            boolean isNull = "null".equals(actualType);
            boolean isEmpty = actualValue.isEmpty();
            boolean isBlank = actualValue.isBlank();
            if ("Not Null".equals(nullRule) && isNull) {
                errors.add("expected not null");
            } else if ("Null".equals(nullRule) && !isNull) {
                errors.add("expected null");
            } else if ("Not Empty".equals(nullRule) && (isNull || isEmpty)) {
                errors.add("expected not empty");
            } else if ("Empty".equals(nullRule) && !isEmpty) {
                errors.add("expected empty");
            } else if ("Not Blank".equals(nullRule) && (isNull || isBlank)) {
                errors.add("expected not blank");
            } else if ("Blank".equals(nullRule) && !isBlank) {
                errors.add("expected blank");
            }
            if (!typeRule.isBlank() && !"Skip".equals(typeRule) && !dbTypeMatches(typeRule, actualType, actualValue)) {
                errors.add("expected " + typeRule);
            }
            if (!expectedValue.isBlank()) {
                String resolvedExpected = resolveVariablesInText(expectedValue);
                if (!resolvedExpected.equals(actualValue)) {
                    errors.add("expected value mismatch");
                }
            }

            if (errors.isEmpty()) {
                dbColumnValidationsTableModel.setValueAt("Passed", row, 6);
                passed++;
            } else {
                dbColumnValidationsTableModel.setValueAt("Failed: " + String.join(", ", errors), row, 6);
                failed++;
            }
        }
        if (selected == 0) {
            JOptionPane.showMessageDialog(this, "Check at least one DB column row before validation.",
                    "DB Column Validations", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (dbColumnValidationsStatusLabel != null) {
            dbColumnValidationsStatusLabel.setText("DB column validation complete. Passed: " + passed + "  Failed: " + failed);
            dbColumnValidationsStatusLabel.setForeground(failed == 0 ? SUCCESS : new Color(196, 70, 54));
        }
    }

    private boolean dbTypeMatches(String expectedType, String actualType, String actualValue) {
        if (expectedType.equals(actualType)) {
            return true;
        }
        if ("number".equals(expectedType) && ("integer".equals(actualType) || "decimal".equals(actualType))) {
            return true;
        }
        if ("datetime".equals(expectedType) && "timestamp".equals(actualType)) {
            return true;
        }
        if ("timestamp".equals(expectedType) && "datetime".equals(actualType)) {
            return true;
        }
        if ("uuid".equals(expectedType)) {
            return actualValue.matches("(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
        }
        if ("json".equals(expectedType)) {
            String trimmed = actualValue.trim();
            if (trimmed.isEmpty()) {
                return false;
            }
            try {
                if (trimmed.startsWith("{")) {
                    new JSONObject(trimmed);
                    return true;
                }
                if (trimmed.startsWith("[")) {
                    new JSONArray(trimmed);
                    return true;
                }
            } catch (Exception ignored) {
                return false;
            }
        }
        return false;
    }

    private void stopDbColumnValidationEditing() {
        if (dbColumnValidationsTable != null && dbColumnValidationsTable.isEditing()) {
            dbColumnValidationsTable.getCellEditor().stopCellEditing();
        }
    }

    private String dbValueType(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
            return "integer";
        }
        if (value instanceof java.math.BigDecimal || value instanceof Float || value instanceof Double) {
            return "decimal";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        if (value instanceof java.sql.Date || value instanceof java.time.LocalDate) {
            return "date";
        }
        if (value instanceof java.sql.Time || value instanceof java.time.LocalTime) {
            return "time";
        }
        if (value instanceof java.sql.Timestamp || value instanceof java.time.Instant
                || value instanceof java.time.OffsetDateTime || value instanceof java.time.ZonedDateTime) {
            return "timestamp";
        }
        if (value instanceof java.util.Date || value instanceof java.time.LocalDateTime) {
            return "datetime";
        }
        if (value instanceof byte[] || value instanceof Byte[]) {
            return "binary";
        }
        return "string";
    }

    private void loadDbColumnOptions() {
        stopDbRuleEditing();
        DbConnectionConfig config = buildDbConnectionConfig();
        String sqlQuery = dbQueryArea.getText();
        runDbValidationButton.setEnabled(false);
        dbSummaryLabel.setText("Total: loading columns...");

        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return dbValidationService.fetchColumnLabels(config, sqlQuery, lastApiResponseBody, snapshotSavedVariables());
            }

            @Override
            protected void done() {
                runDbValidationButton.setEnabled(true);
                try {
                    updateDbColumnOptions(get());
                    dbSummaryLabel.setText("Total: columns loaded");
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            dbColumnOptions.size() + " DB column(s) loaded.");
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    dbSummaryLabel.setText("Total: 0");
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            cause.getMessage(), "Load DB Columns Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private DbConnectionConfig buildDbConnectionConfig() {
        DbConnectionConfig config = new DbConnectionConfig();
        config.databaseType = String.valueOf(dbTypeDropdown.getSelectedItem());
        config.jdbcUrl = jdbcUrlField.getText().trim();
        config.username = dbUsernameField.getText().trim();
        config.password = new String(dbPasswordField.getPassword());
        config.driverClass = driverClassField.getText().trim();
        return config;
    }

    private void applyDbConnectionConfig(DbConnectionConfig config) {
        if (config == null) {
            return;
        }
        String databaseType = config.databaseType == null || config.databaseType.isBlank()
                ? "Custom" : config.databaseType;
        boolean matched = false;
        for (int i = 0; i < dbTypeDropdown.getItemCount(); i++) {
            if (databaseType.equals(dbTypeDropdown.getItemAt(i))) {
                dbTypeDropdown.setSelectedIndex(i);
                matched = true;
                break;
            }
        }
        if (!matched) {
            dbTypeDropdown.setSelectedItem("Custom");
        }
        jdbcUrlField.setText(config.jdbcUrl == null ? "" : config.jdbcUrl);
        dbUsernameField.setText(config.username == null ? "" : config.username);
        dbPasswordField.setText(config.password == null ? "" : config.password);
        driverClassField.setText(config.driverClass == null ? "" : config.driverClass);
        dbPasswordField.setEchoChar(dbDefaultEchoChar);
    }

    private List<DbValidationRule> collectDbRules() {
        List<DbValidationRule> rules = new ArrayList<>();
        for (int row = 0; row < dbRulesTableModel.getRowCount(); row++) {
            if (!isRowChecked(dbRulesTableModel, row)) {
                continue;
            }
            String apiField = stringCellValue(dbRulesTableModel, row, 1);
            String dbColumn = stringCellValue(dbRulesTableModel, row, 2);
            String operator = stringCellValue(dbRulesTableModel, row, 3);
            String description = stringCellValue(dbRulesTableModel, row, 4);
            if (apiField.isBlank() && dbColumn.isBlank() && operator.isBlank() && description.isBlank()) {
                continue;
            }

            DbValidationRule rule = new DbValidationRule();
            rule.apiField = apiField;
            rule.dbColumn = dbColumn;
            rule.operator = operator.isBlank() ? "=" : operator;
            rule.description = description;
            rules.add(rule);
        }
        return rules;
    }

    private void saveDbRules() {
        stopDbRuleEditing();
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("db-validation-rules.json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            JSONArray rulesJson = new JSONArray();
            stopDbRuleEditing();
            for (int row = 0; row < dbRulesTableModel.getRowCount(); row++) {
                String apiField = stringCellValue(dbRulesTableModel, row, 1);
                String dbColumn = stringCellValue(dbRulesTableModel, row, 2);
                String operator = stringCellValue(dbRulesTableModel, row, 3);
                String description = stringCellValue(dbRulesTableModel, row, 4);
                if (apiField.isBlank() && dbColumn.isBlank() && operator.isBlank() && description.isBlank()) {
                    continue;
                }
                JSONObject json = new JSONObject();
                json.put("enabled", isRowChecked(dbRulesTableModel, row));
                json.put("apiField", apiField);
                json.put("dbColumn", dbColumn);
                json.put("operator", operator.isBlank() ? "=" : operator);
                json.put("description", description);
                rulesJson.put(json);
            }
            Files.writeString(chooser.getSelectedFile().toPath(), rulesJson.toString(2));
            JOptionPane.showMessageDialog(this, "DB validation rules saved successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Rules Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDbRules() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            JSONArray rulesJson = new JSONArray(Files.readString(chooser.getSelectedFile().toPath()));
            dbRulesTableModel.setRowCount(0);
            for (int i = 0; i < rulesJson.length(); i++) {
                JSONObject json = rulesJson.getJSONObject(i);
                dbRulesTableModel.addRow(new Object[]{
                        json.optBoolean("enabled", true),
                        json.optString("apiField"),
                        json.optString("dbColumn"),
                        json.optString("operator", "="),
                        json.optString("description")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Load Rules Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSelectedDbRule() {
        stopDbRuleEditing();
        int selectedRow = dbRulesTable.getSelectedRow();
        if (selectedRow >= 0) {
            dbRulesTableModel.removeRow(dbRulesTable.convertRowIndexToModel(selectedRow));
            return;
        }
        if (dbRulesTableModel.getRowCount() > 0) {
            dbRulesTableModel.removeRow(dbRulesTableModel.getRowCount() - 1);
        }
    }

    private void populateDefaultDbRules() {
        if (dbRulesTableModel == null || dbRulesTableModel.getRowCount() > 0) {
            return;
        }
        dbRulesTableModel.addRow(new Object[]{Boolean.TRUE, "id", "id", "=", "User ID should match"});
        dbRulesTableModel.addRow(new Object[]{Boolean.TRUE, "name", "name", "=", "User name should match"});
        dbRulesTableModel.addRow(new Object[]{Boolean.TRUE, "email", "email", "contains", "Email should contain"});
        dbRulesTableModel.addRow(new Object[]{Boolean.TRUE, "updatedAt", "updated_at", ">=", "Updated timestamp should be newer or equal"});
    }

    private JButton createDbPasswordToggleButton() {
        JButton toggle = createSecondaryButton("Show");
        toggle.addActionListener(e -> {
            if (dbPasswordField.getEchoChar() == 0) {
                dbPasswordField.setEchoChar(dbDefaultEchoChar);
                toggle.setText("Show");
            } else {
                dbPasswordField.setEchoChar((char) 0);
                toggle.setText("Hide");
            }
        });
        return toggle;
    }

    private void applyDbTypeDefaults() {
        String type = String.valueOf(dbTypeDropdown.getSelectedItem());
        if ("MySQL".equals(type)) {
            if (jdbcUrlField.getText().isBlank() || jdbcUrlField.getText().startsWith("jdbc:")) {
                jdbcUrlField.setText("jdbc:mysql://localhost:3306/testdb");
            }
            driverClassField.setText("com.mysql.cj.jdbc.Driver");
        } else if ("PostgreSQL".equals(type)) {
            jdbcUrlField.setText("jdbc:postgresql://localhost:5432/testdb");
            driverClassField.setText("org.postgresql.Driver");
        } else if ("Oracle".equals(type)) {
            jdbcUrlField.setText("jdbc:oracle:thin:@localhost:1521/orclpdb");
            driverClassField.setText("oracle.jdbc.OracleDriver");
        } else if ("SQL Server".equals(type)) {
            jdbcUrlField.setText("jdbc:sqlserver://localhost:1433;databaseName=testdb;encrypt=false");
            driverClassField.setText("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
    }

    private void addDbField(JPanel panel, int row, String labelText, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel(labelText);
        label.setFont(UI_FONT);
        panel.add(label, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        field.setPreferredSize(new Dimension(360, 42));
        panel.add(field, gbc);
    }

    private void stopDbRuleEditing() {
        if (dbRulesTable != null && dbRulesTable.isEditing()) {
            dbRulesTable.getCellEditor().stopCellEditing();
        }
    }

    private JComboBox<String> createDbColumnDropdown() {
        List<String> options = new ArrayList<>();
        for (String columnName : dbColumnOptions) {
            options.add(columnName);
            for (int rowIndex = 0; rowIndex < dbColumnOptionRowCount; rowIndex++) {
                options.add(columnName + "[" + rowIndex + "]");
            }
        }
        JComboBox<String> dropdown = new JComboBox<>(options.toArray(new String[0]));
        dropdown.setEditable(true);
        dropdown.setFont(UI_FONT);
        return dropdown;
    }

    private void updateDbColumnOptionsFromRows(List<Map<String, Object>> dbRows) {
        if (dbRows == null || dbRows.isEmpty()) {
            return;
        }
        dbColumnOptionRowCount = dbRows.size();
        updateDbColumnOptions(new ArrayList<>(dbRows.get(0).keySet()));
    }

    private void updateDbColumnOptions(List<String> columnLabels) {
        if (columnLabels == null || columnLabels.isEmpty()) {
            return;
        }

        stopDbRuleEditing();
        dbColumnOptions.clear();
        for (String columnLabel : columnLabels) {
            if (columnLabel == null || columnLabel.isBlank() || dbColumnOptions.contains(columnLabel)) {
                continue;
            }
            dbColumnOptions.add(columnLabel);
        }
        if (dbRulesTable != null) {
            dbRulesTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(createDbColumnDropdown()));
        }
    }

    private boolean isRowChecked(DefaultTableModel model, int row) {
        Object value = model.getValueAt(row, 0);
        return !(value instanceof Boolean) || (Boolean) value;
    }

    private void setAllRowsChecked(JTable table, DefaultTableModel model, boolean checked) {
        if (model == null) {
            return;
        }
        if (table != null && table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        for (int row = 0; row < model.getRowCount(); row++) {
            model.setValueAt(checked, row, 0);
        }
    }

    private String stringCellValue(DefaultTableModel model, int row, int column) {
        Object value = model.getValueAt(row, column);
        return value == null ? "" : String.valueOf(value).trim();
    }

    private void startWebRecording() {
        String startUrl = webStartUrlField.getText().trim();
        if (startUrl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Start URL is required before recording.",
                    "Web Recording", JOptionPane.WARNING_MESSAGE);
            return;
        }

        webRecordButton.setEnabled(false);
        webAttachButton.setEnabled(false);
        webStopButton.setEnabled(true);
        webStopNoBrowserCloseButton.setEnabled(true);
        webRecorderStatusLabel.setText("Launching Playwright browser...");
        webRecorderStatusLabel.setForeground(PRIMARY);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                playwrightRecorderController.startRecording(startUrl, new PlaywrightRecorderController.RecorderListener() {
                    @Override
                    public void onStatus(String message) {
                        SwingUtilities.invokeLater(() -> {
                            webRecorderStatusLabel.setText(message);
                            webRecorderStatusLabel.setForeground(PRIMARY);
                        });
                    }

                    @Override
                    public void onStepCaptured(WebTestStep step) {
                        SwingUtilities.invokeLater(() -> appendWebStepToTable(step));
                    }

                    @Override
                    public void onRecordingStopped() {
                        SwingUtilities.invokeLater(() -> {
                            webRecordButton.setEnabled(true);
                            webAttachButton.setEnabled(true);
                            webStopButton.setEnabled(false);
                            webStopNoBrowserCloseButton.setEnabled(false);
                            webRecorderStatusLabel.setText("Recording stopped");
                            webRecorderStatusLabel.setForeground(new Color(95, 103, 120));
                        });
                    }

                    @Override
                    public void onUrlChanged(String url) {
                        SwingUtilities.invokeLater(() -> webBrowserUrlLabel.setText("Browser URL: " + url));
                    }

                    @Override
                    public void onError(String message) {
                        SwingUtilities.invokeLater(() -> {
                            webRecorderStatusLabel.setText("Recorder error");
                            webRecorderStatusLabel.setForeground(new Color(196, 70, 54));
                            JOptionPane.showMessageDialog(ApiValidatorUI.this, message,
                                    "Web Recorder Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                });
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    webRecordButton.setEnabled(true);
                    webAttachButton.setEnabled(true);
                    webStopButton.setEnabled(false);
                    webStopNoBrowserCloseButton.setEnabled(false);
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            buildPlaywrightErrorMessage(cause.getMessage()),
                            "Web Recording Failed", JOptionPane.ERROR_MESSAGE);
                    webRecorderStatusLabel.setText("Recorder failed to start");
                    webRecorderStatusLabel.setForeground(new Color(196, 70, 54));
                }
            }
        };
        worker.execute();
    }

    private void startAttachedWebRecording() {
        String cdpEndpoint = webCdpEndpointField.getText().trim();
        if (cdpEndpoint.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the active browser CDP URL before attaching.",
                    "Web Recording", JOptionPane.WARNING_MESSAGE);
            return;
        }

        webRecordButton.setEnabled(false);
        webAttachButton.setEnabled(false);
        webStopButton.setEnabled(true);
        webStopNoBrowserCloseButton.setEnabled(true);
        webRecorderStatusLabel.setText("Attaching to active browser...");
        webRecorderStatusLabel.setForeground(PRIMARY);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                playwrightRecorderController.startAttachedRecording(cdpEndpoint, new PlaywrightRecorderController.RecorderListener() {
                    @Override
                    public void onStatus(String message) {
                        SwingUtilities.invokeLater(() -> {
                            webRecorderStatusLabel.setText(message);
                            webRecorderStatusLabel.setForeground(PRIMARY);
                        });
                    }

                    @Override
                    public void onStepCaptured(WebTestStep step) {
                        SwingUtilities.invokeLater(() -> appendWebStepToTable(step));
                    }

                    @Override
                    public void onRecordingStopped() {
                        SwingUtilities.invokeLater(() -> {
                            webRecordButton.setEnabled(true);
                            webAttachButton.setEnabled(true);
                            webStopButton.setEnabled(false);
                            webStopNoBrowserCloseButton.setEnabled(false);
                            webRecorderStatusLabel.setText("Recording stopped");
                            webRecorderStatusLabel.setForeground(new Color(95, 103, 120));
                        });
                    }

                    @Override
                    public void onUrlChanged(String url) {
                        SwingUtilities.invokeLater(() -> webBrowserUrlLabel.setText("Browser URL: " + url));
                    }

                    @Override
                    public void onError(String message) {
                        SwingUtilities.invokeLater(() -> {
                            webRecorderStatusLabel.setText("Recorder error");
                            webRecorderStatusLabel.setForeground(new Color(196, 70, 54));
                            JOptionPane.showMessageDialog(ApiValidatorUI.this, message,
                                    "Web Recorder Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                });
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    webRecordButton.setEnabled(true);
                    webAttachButton.setEnabled(true);
                    webStopButton.setEnabled(false);
                    webStopNoBrowserCloseButton.setEnabled(false);
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            buildAttachBrowserErrorMessage(cause.getMessage()),
                            "Attach Browser Failed", JOptionPane.ERROR_MESSAGE);
                    webRecorderStatusLabel.setText("Attach failed");
                    webRecorderStatusLabel.setForeground(new Color(196, 70, 54));
                }
            }
        };
        worker.execute();
    }

    private void launchDebugChrome() {
        Path chromePath = Path.of("C:", "Program Files", "Google", "Chrome", "Application", "chrome.exe");
        Path profilePath = Path.of("C:", "temp", "chrome_debug_profile");

        if (!Files.exists(chromePath)) {
            JOptionPane.showMessageDialog(this,
                    "Chrome was not found at:\n" + chromePath,
                    "Launch Debug Chrome Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Files.createDirectories(profilePath);
            new ProcessBuilder(
                    chromePath.toString(),
                    "--remote-debugging-port=9222",
                    "--user-data-dir=" + profilePath
            ).start();
            webCdpEndpointField.setText("http://127.0.0.1:9222");
            webRecorderStatusLabel.setText("Debug Chrome launched. Click Attach Active Browser when it is ready.");
            webRecorderStatusLabel.setForeground(PRIMARY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Launch Debug Chrome Failed", JOptionPane.ERROR_MESSAGE);
            webRecorderStatusLabel.setText("Debug Chrome launch failed");
            webRecorderStatusLabel.setForeground(new Color(196, 70, 54));
        }
    }

    private void stopWebRecording() {
        playwrightRecorderController.stopRecording();
        webRecordButton.setEnabled(true);
        webAttachButton.setEnabled(true);
        webStopButton.setEnabled(false);
        webStopNoBrowserCloseButton.setEnabled(false);
        webRecorderStatusLabel.setText("Recording stopped");
        webRecorderStatusLabel.setForeground(new Color(95, 103, 120));
    }

    private void stopWebRecordingWithoutClosingBrowser() {
        playwrightRecorderController.stopRecordingWithoutClosingBrowser();
        webRecordButton.setEnabled(true);
        webAttachButton.setEnabled(true);
        webStopButton.setEnabled(false);
        webStopNoBrowserCloseButton.setEnabled(false);
        webRecorderStatusLabel.setText("Recording stopped. Browser left open.");
        webRecorderStatusLabel.setForeground(new Color(95, 103, 120));
    }

    private void runWebTest() {
        WebTestCase testCase = buildCurrentWebTestCase();
        if (testCase.steps.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Record or add at least one step before running the web test.",
                    "Web Testing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        webRunButton.setEnabled(false);
        webRetestButton.setEnabled(false);
        webStopRunButton.setEnabled(true);
        webRunSummaryLabel.setText("Total: running...");
        webRunPassedLabel.setText("Passed: --");
        webRunFailedLabel.setText("Failed: --");
        webResultsTableModel.setRowCount(0);

        SwingWorker<WebTestRunReport, Void> worker = new SwingWorker<>() {
            @Override
            protected WebTestRunReport doInBackground() throws Exception {
                return playwrightRecorderController.runTest(
                        testCase,
                        webHeadlessCheckbox.isSelected(),
                        webSlowMoCheckbox.isSelected() ? 250 : 0
                );
            }

            @Override
            protected void done() {
                webRunButton.setEnabled(true);
                webRetestButton.setEnabled(true);
                webStopRunButton.setEnabled(false);
                try {
                    renderWebRunReport(get());
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            buildPlaywrightErrorMessage(cause.getMessage()),
                            "Web Test Failed", JOptionPane.ERROR_MESSAGE);
                    webRunSummaryLabel.setText("Total: 0");
                    webRunPassedLabel.setText("Passed: 0");
                    webRunFailedLabel.setText("Failed: 0");
                }
            }
        };
        worker.execute();
    }

    private void stopWebTest() {
        webStopRunButton.setEnabled(false);
        webRunSummaryLabel.setText("Total: stopping...");
        playwrightRecorderController.stopRunningWebTest();
    }

    private void retestSelectedWebSteps() {
        if (webResultsTable == null || webResultsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Run the web test before selecting result rows to re-test.",
                    "Web Re-Test", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int[] selectedRows = webResultsTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select one or more Step Results rows to re-test.",
                    "Web Re-Test", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cdpEndpoint = webCdpEndpointField.getText().trim();
        if (cdpEndpoint.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Active Browser CDP URL before re-testing.",
                    "Web Re-Test", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stopWebResultEditing();
        List<Integer> modelRows = new ArrayList<>();
        List<WebTestStep> selectedSteps = new ArrayList<>();
        List<Integer> capturedStepRows = new ArrayList<>();
        for (int viewRow : selectedRows) {
            int modelRow = webResultsTable.convertRowIndexToModel(viewRow);
            int stepNumber;
            try {
                stepNumber = Integer.parseInt(stringCellValue(webResultsTableModel, modelRow, 0));
            } catch (NumberFormatException e) {
                continue;
            }
            int stepIndex = stepNumber - 1;
            if (stepIndex < 0 || stepIndex >= webStepsTableModel.getRowCount()) {
                continue;
            }
            WebTestStep step = readWebStepFromTable(stepIndex);
            step.selector = stringCellValue(webResultsTableModel, modelRow, 2);
            step.value = stringCellValue(webResultsTableModel, modelRow, 3);
            step.selector = resolveVariablesInText(step.selector);
            if (!isGetTextWebAction(step.action)) {
                step.value = resolveVariablesInText(step.value);
            }
            modelRows.add(modelRow);
            selectedSteps.add(step);
            capturedStepRows.add(stepIndex);
            webResultsTableModel.setValueAt("RE-TESTING", modelRow, 4);
            webResultsTableModel.setValueAt("Queued for re-test", modelRow, 5);
            webResultsTableModel.setValueAt("--", modelRow, 6);
        }

        if (selectedSteps.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selected result rows do not map to captured steps.",
                    "Web Re-Test", JOptionPane.WARNING_MESSAGE);
            return;
        }

        webRunButton.setEnabled(false);
        webRetestButton.setEnabled(false);
        webStopRunButton.setEnabled(true);
        webRunSummaryLabel.setText("Total: re-testing " + selectedSteps.size() + "...");

        SwingWorker<WebTestRunReport, Void> worker = new SwingWorker<>() {
            @Override
            protected WebTestRunReport doInBackground() throws Exception {
                return playwrightRecorderController.retestStepsOnActiveBrowser(
                        cdpEndpoint,
                        selectedSteps,
                        webSlowMoCheckbox.isSelected() ? 250 : 0
                );
            }

            @Override
            protected void done() {
                webRunButton.setEnabled(true);
                webRetestButton.setEnabled(true);
                webStopRunButton.setEnabled(false);
                try {
                    WebTestRunReport report = get();
                    refreshSelectedWebResultRows(modelRows, capturedStepRows, report);
                    updateWebResultSummary();
                    if (report.lastScreenshotPath != null) {
                        webScreenshotLabel.setText("Last screenshot: " + report.lastScreenshotPath);
                    }
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    for (int modelRow : modelRows) {
                        if ("RE-TESTING".equals(stringCellValue(webResultsTableModel, modelRow, 4))) {
                            webResultsTableModel.setValueAt("FAIL", modelRow, 4);
                            webResultsTableModel.setValueAt("Re-test did not run: " + buildAttachBrowserErrorMessage(cause.getMessage()), modelRow, 5);
                            webResultsTableModel.setValueAt("--", modelRow, 6);
                        }
                    }
                    JOptionPane.showMessageDialog(ApiValidatorUI.this,
                            buildAttachBrowserErrorMessage(cause.getMessage()),
                            "Web Re-Test Failed", JOptionPane.ERROR_MESSAGE);
                    updateWebResultSummary();
                }
            }
        };
        worker.execute();
    }

    private void refreshSelectedWebResultRows(List<Integer> modelRows, List<Integer> capturedStepRows, WebTestRunReport report) {
        int count = Math.min(modelRows.size(), report.results.size());
        for (int i = 0; i < count; i++) {
            WebTestExecutionResult result = report.results.get(i);
            int modelRow = modelRows.get(i);
            webResultsTableModel.setValueAt(result.action, modelRow, 1);
            webResultsTableModel.setValueAt(result.selector, modelRow, 2);
            webResultsTableModel.setValueAt(result.expectedValue, modelRow, 3);
            webResultsTableModel.setValueAt(result.passed ? "PASS" : "FAIL", modelRow, 4);
            webResultsTableModel.setValueAt(result.message, modelRow, 5);
            webResultsTableModel.setValueAt(String.format("%.2f s", result.durationMs / 1000.0), modelRow, 6);
            if (result.passed && i < capturedStepRows.size()) {
                storeCapturedWebVariable(result, null);
                int capturedStepRow = capturedStepRows.get(i);
                webStepsTableModel.setValueAt(result.selector, capturedStepRow, 2);
                webStepsTableModel.setValueAt(result.expectedValue, capturedStepRow, 3);
            }
        }
        for (int i = count; i < modelRows.size(); i++) {
            int modelRow = modelRows.get(i);
            webResultsTableModel.setValueAt("STOPPED", modelRow, 4);
            webResultsTableModel.setValueAt("Re-test stopped before this step ran", modelRow, 5);
            webResultsTableModel.setValueAt("--", modelRow, 6);
        }
    }

    private void updateWebResultSummary() {
        int total = webResultsTableModel.getRowCount();
        int passed = 0;
        int failed = 0;
        for (int row = 0; row < total; row++) {
            String status = stringCellValue(webResultsTableModel, row, 4);
            if ("PASS".equals(status)) {
                passed++;
            } else if ("FAIL".equals(status)) {
                failed++;
            }
        }
        webRunSummaryLabel.setText("Total: " + total);
        webRunPassedLabel.setText("Passed: " + passed);
        webRunFailedLabel.setText("Failed: " + failed);
    }

    private void stopWebResultEditing() {
        if (webResultsTable != null && webResultsTable.isEditing()) {
            webResultsTable.getCellEditor().stopCellEditing();
        }
    }

    private void configureWebResultsEditing() {
        JTextField editorField = new JTextField();
        editorField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        DefaultCellEditor textEditor = new DefaultCellEditor(editorField);
        textEditor.setClickCountToStart(2);
        webResultsTable.getColumnModel().getColumn(2).setCellEditor(textEditor);
        webResultsTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()) {
            {
                setClickCountToStart(2);
            }
        });
        webResultsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                int viewRow = webResultsTable.rowAtPoint(e.getPoint());
                int viewColumn = webResultsTable.columnAtPoint(e.getPoint());
                if (viewRow < 0 || viewColumn < 0) {
                    return;
                }
                int modelColumn = webResultsTable.convertColumnIndexToModel(viewColumn);
                if (modelColumn != 2 && modelColumn != 3) {
                    return;
                }
                webResultsTable.editCellAt(viewRow, viewColumn);
                Component editor = webResultsTable.getEditorComponent();
                if (editor != null) {
                    editor.requestFocusInWindow();
                    if (editor instanceof JTextField textField) {
                        textField.selectAll();
                    }
                }
            }
        });
    }

    private void configureWebResultsCopying() {
        webResultsTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                        "copyWebResultCells");
        webResultsTable.getActionMap().put("copyWebResultCells", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                copySelectedWebResultCells();
            }
        });

        JPopupMenu popup = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copy Selected Cell(s)");
        copyItem.addActionListener(e -> copySelectedWebResultCells());
        popup.add(copyItem);
        webResultsTable.setComponentPopupMenu(popup);
    }

    private void copySelectedWebResultCells() {
        if (webResultsTable == null) {
            return;
        }
        int[] rows = webResultsTable.getSelectedRows();
        int[] columns = webResultsTable.getSelectedColumns();
        if (rows.length == 0 || columns.length == 0) {
            return;
        }

        StringBuilder text = new StringBuilder();
        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            if (rowIndex > 0) {
                text.append(System.lineSeparator());
            }
            for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
                if (columnIndex > 0) {
                    text.append('\t');
                }
                int modelRow = webResultsTable.convertRowIndexToModel(rows[rowIndex]);
                int modelColumn = webResultsTable.convertColumnIndexToModel(columns[columnIndex]);
                Object value = webResultsTableModel.getValueAt(modelRow, modelColumn);
                text.append(value == null ? "" : value);
            }
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new java.awt.datatransfer.StringSelection(text.toString()), null
        );
    }

    private void renderWebRunReport(WebTestRunReport report) {
        webResultsTableModel.setRowCount(0);
        for (int i = 0; i < report.results.size(); i++) {
            WebTestExecutionResult result = report.results.get(i);
            storeCapturedWebVariable(result, null);
            webResultsTableModel.addRow(new Object[]{
                    i + 1,
                    result.action,
                    result.selector,
                    result.expectedValue,
                    result.passed ? "PASS" : "FAIL",
                    result.message,
                    String.format("%.2f s", result.durationMs / 1000.0)
            });
        }
        webRunSummaryLabel.setText((report.stopped ? "Stopped: " : "Total: ") + report.total);
        webRunPassedLabel.setText("Passed: " + report.passed);
        webRunFailedLabel.setText("Failed: " + report.failed);
    }

    private void addWebStep() {
        WebTestStep step = promptForWebStep(null);
        if (step != null) {
            appendWebStepToTable(step);
        }
    }

    private void editSelectedWebStep() {
        int selectedRow = webStepsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a web step to edit.", "Web Testing", JOptionPane.WARNING_MESSAGE);
            return;
        }
        WebTestStep existing = readWebStepFromTable(selectedRow);
        WebTestStep updated = promptForWebStep(existing);
        if (updated != null) {
            writeWebStepToTable(selectedRow, updated);
        }
    }

    private void deleteSelectedWebStep() {
        int selectedRow = webStepsTable.getSelectedRow();
        if (selectedRow >= 0) {
            webStepsTableModel.removeRow(selectedRow);
            renumberWebSteps();
        }
    }

    private void moveSelectedWebStep(int direction) {
        int selectedRow = webStepsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        int targetRow = selectedRow + direction;
        if (targetRow < 0 || targetRow >= webStepsTableModel.getRowCount()) {
            return;
        }
        webStepsTableModel.moveRow(selectedRow, selectedRow, targetRow);
        renumberWebSteps();
        webStepsTable.setRowSelectionInterval(targetRow, targetRow);
    }

    private void addWebScreenshotStep() {
        WebTestStep step = new WebTestStep();
        step.action = "Screenshot";
        step.selector = "page";
        step.value = "web-test-shot.png";
        step.note = "Captured during run";
        appendWebStepToTable(step);
    }

    private void clearWebSteps() {
        webStepsTableModel.setRowCount(0);
        if (webResultsTableModel != null) {
            webResultsTableModel.setRowCount(0);
        }
        if (webRunSummaryLabel != null) {
            webRunSummaryLabel.setText("Total: 0");
            webRunPassedLabel.setText("Passed: 0");
            webRunFailedLabel.setText("Failed: 0");
            webScreenshotLabel.setText("Last screenshot: not captured yet");
        }
    }

    private void saveWebRecording() {
        WebTestCase testCase = buildCurrentWebTestCase();
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("web-recording.json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("testName", testCase.testName);
            json.put("startUrl", testCase.startUrl);
            JSONArray steps = new JSONArray();
            for (WebTestStep step : testCase.steps) {
                JSONObject stepJson = new JSONObject();
                stepJson.put("action", step.action);
                stepJson.put("selector", step.selector);
                stepJson.put("value", step.value);
                stepJson.put("note", step.note);
                stepJson.put("suggested", step.suggested);
                steps.put(stepJson);
            }
            json.put("steps", steps);
            Files.writeString(chooser.getSelectedFile().toPath(), json.toString(2));
            webRecorderStatusLabel.setText("Recording saved");
            webRecorderStatusLabel.setForeground(SUCCESS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Recording Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadWebRecording() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            WebTestCase testCase = readWebRecording(chooser.getSelectedFile().toPath());
            webTestNameField.setText(testCase.testName == null || testCase.testName.isBlank()
                    ? "Loaded Web Test" : testCase.testName);
            webStartUrlField.setText(testCase.startUrl == null ? "" : testCase.startUrl);
            webStepsTableModel.setRowCount(0);
            for (WebTestStep step : testCase.steps) {
                appendWebStepToTable(step);
            }
            webRecorderStatusLabel.setText("Recording loaded");
            webRecorderStatusLabel.setForeground(PRIMARY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Load Recording Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mergeWebRecording() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Recording to Merge");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            WebTestCase testCase = readWebRecording(chooser.getSelectedFile().toPath());
            if (webTestNameField.getText().trim().isBlank() && testCase.testName != null && !testCase.testName.isBlank()) {
                webTestNameField.setText(testCase.testName);
            }
            if (webStartUrlField.getText().trim().isBlank() && testCase.startUrl != null && !testCase.startUrl.isBlank()) {
                webStartUrlField.setText(testCase.startUrl);
            }
            int beforeCount = webStepsTableModel.getRowCount();
            for (WebTestStep step : testCase.steps) {
                appendWebStepToTable(step);
            }
            int mergedCount = webStepsTableModel.getRowCount() - beforeCount;
            webRecorderStatusLabel.setText("Merged " + mergedCount + " step(s) into current recording");
            webRecorderStatusLabel.setForeground(SUCCESS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Merge Recording Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private WebTestCase readWebRecording(Path path) throws Exception {
        JSONObject json = new JSONObject(Files.readString(path));
        WebTestCase testCase = new WebTestCase();
        testCase.testName = json.optString("testName", "Loaded Web Test");
        testCase.startUrl = json.optString("startUrl");
        JSONArray steps = json.optJSONArray("steps");
        if (steps != null) {
            for (int i = 0; i < steps.length(); i++) {
                JSONObject stepJson = steps.getJSONObject(i);
                WebTestStep step = new WebTestStep();
                step.action = stepJson.optString("action");
                step.selector = stepJson.optString("selector");
                step.value = stepJson.optString("value");
                step.note = stepJson.optString("note");
                step.suggested = stepJson.optBoolean("suggested");
                testCase.steps.add(step);
            }
        }
        return testCase;
    }

    private WebTestCase buildCurrentWebTestCase() {
        WebTestCase testCase = new WebTestCase();
        testCase.testName = webTestNameField.getText().trim();
        testCase.startUrl = webStartUrlField.getText().trim();
        for (int row = 0; row < webStepsTableModel.getRowCount(); row++) {
            WebTestStep step = readWebStepFromTable(row);
            step.selector = resolveVariablesInText(step.selector);
            if (!isGetTextWebAction(step.action)) {
                step.value = resolveVariablesInText(step.value);
            }
            testCase.steps.add(step);
        }
        return testCase;
    }

    private WebTestCase buildCurrentRawWebTestCase() {
        WebTestCase testCase = new WebTestCase();
        testCase.testName = webTestNameField.getText().trim();
        testCase.startUrl = webStartUrlField.getText().trim();
        for (int row = 0; row < webStepsTableModel.getRowCount(); row++) {
            testCase.steps.add(readWebStepFromTable(row));
        }
        return testCase;
    }

    private WebTestStep readWebStepFromTable(int row) {
        WebTestStep step = new WebTestStep();
        step.action = stringCellValue(webStepsTableModel, row, 1);
        step.selector = stringCellValue(webStepsTableModel, row, 2);
        step.value = stringCellValue(webStepsTableModel, row, 3);
        step.note = stringCellValue(webStepsTableModel, row, 4);
        step.suggested = step.note != null && step.note.toLowerCase().contains("suggested");
        return step;
    }

    private void writeWebStepToTable(int row, WebTestStep step) {
        webStepsTableModel.setValueAt(row + 1, row, 0);
        webStepsTableModel.setValueAt(step.action, row, 1);
        webStepsTableModel.setValueAt(step.selector, row, 2);
        webStepsTableModel.setValueAt(step.value, row, 3);
        webStepsTableModel.setValueAt(step.note, row, 4);
    }

    private void appendWebStepToTable(WebTestStep step) {
        webStepsTableModel.addRow(new Object[]{
                webStepsTableModel.getRowCount() + 1,
                step.action,
                step.selector,
                step.value,
                step.note
        });
    }

    private void renumberWebSteps() {
        for (int i = 0; i < webStepsTableModel.getRowCount(); i++) {
            webStepsTableModel.setValueAt(i + 1, i, 0);
        }
    }

    private WebTestStep promptForWebStep(WebTestStep existing) {
        JComboBox<String> actionBox = new JComboBox<>(new String[]{"Navigate", "Type", "Click", "Select Option", "Validate Text", "Get Text", "Screenshot"});
        JTextField selectorField = new JTextField(existing == null ? "" : existing.selector);
        JTextField valueField = new JTextField(existing == null ? "" : existing.value);
        JTextField noteField = new JTextField(existing == null ? "" : existing.note);
        if (existing != null && existing.action != null) {
            actionBox.setSelectedItem(existing.action);
        }

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 8));
        panel.add(createLabeledPanel("Action", actionBox));
        panel.add(createLabeledPanel("Selector", selectorField));
        panel.add(createLabeledPanel("Value / Expected", valueField));
        panel.add(createLabeledPanel("Notes", noteField));

        int option = JOptionPane.showConfirmDialog(this, panel,
                existing == null ? "Add Web Step" : "Edit Web Step",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        WebTestStep step = new WebTestStep();
        step.action = String.valueOf(actionBox.getSelectedItem());
        step.selector = selectorField.getText().trim();
        step.value = valueField.getText().trim();
        step.note = noteField.getText().trim();
        return step;
    }

    private boolean isGetTextWebAction(String action) {
        return "get text".equalsIgnoreCase(action == null ? "" : action.trim());
    }

    private void storeCapturedWebVariable(WebTestExecutionResult result, Map<String, String> runnerVariables) {
        if (result == null || !result.passed || result.capturedVariableName == null || result.capturedVariableName.isBlank()) {
            return;
        }
        String variableName = normalizeVariableName(result.capturedVariableName);
        if (variableName.isBlank()) {
            return;
        }
        String value = result.capturedVariableValue == null ? "" : result.capturedVariableValue;
        if (runnerVariables != null) {
            runnerVariables.put(variableName, value);
        }
        savedVariables.put(variableName, value);
        savedVariablePaths.put(variableName, "web." + (result.selector == null ? "" : result.selector));
        savedVariableTypes.put(variableName, "string");
        refreshSavedVariablesView();
    }

    private String buildPlaywrightErrorMessage(String originalMessage) {
        String message = originalMessage == null ? "Playwright failed to start." : originalMessage;
        String normalized = message.toLowerCase();
        if (normalized.contains("executable doesn't exist")
                || normalized.contains("please run the following command")
                || normalized.contains("browserType.launch")) {
            return message + "\n\nInstall Playwright Chromium with:\n"
                    + "mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args=\"install chromium\"";
        }
        return message;
    }

    private String buildAttachBrowserErrorMessage(String originalMessage) {
        String message = originalMessage == null ? "Could not attach to the active browser." : originalMessage;
        return message + "\n\nNo Chromium browser is reachable on the CDP port yet.\n"
                + "Close any browser window that is using the same profile, then start Chrome or Edge with remote debugging enabled.\n"
                + "Attach to: http://127.0.0.1:9222\n\n"
                + "Example:\n"
                + "chrome.exe --remote-debugging-port=9222 --user-data-dir=%TEMP%\\testweave-cdp\n"
                + "msedge.exe --remote-debugging-port=9222 --user-data-dir=%TEMP%\\testweave-cdp";
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveRequest() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("request.json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("method", methodDropdown.getSelectedItem());
            requestData.put("url", endpointField.getText().trim());
            requestData.put("authType", authTypeDropdown.getSelectedItem());
            requestData.put("token", new String(tokenField.getPassword()));
            requestData.put("headers", parseHeaders(headersArea.getText()));
            requestData.put("body", bodyArea.getText());

            String json = apiService.prettyPrintJson(new org.json.JSONObject(requestData).toString());
            Files.writeString(chooser.getSelectedFile().toPath(), json);
            JOptionPane.showMessageDialog(this, "Request saved successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveResponse() {
        String response = rawResponseArea.getText();
        if (response == null || response.isBlank() || "Sending request...".equals(response)) {
            JOptionPane.showMessageDialog(this, "No response is available to save.", "Save Response", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("response.json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            Files.writeString(chooser.getSelectedFile().toPath(), response);
            JOptionPane.showMessageDialog(this, "Response saved successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearApiForm() {
        methodDropdown.setSelectedItem("GET");
        endpointField.setText("");
        authTypeDropdown.setSelectedItem("None");
        tokenField.setText("");
        headersArea.setText("");
        bodyArea.setText("");
        lastApiResponseBody = null;
        renderError("Response cleared.");
        statusValueLabel.setText("Cleared");
        statusValueLabel.setForeground(new Color(100, 110, 125));
        timeValueLabel.setText("--");
        sizeValueLabel.setText("--");
        tokenField.setEnabled(false);
        updateRequestBodyState();
    }

    private void beautifyRequestBody() {
        try {
            String pretty = apiService.prettyPrintJson(bodyArea.getText());
            bodyArea.setText(pretty);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Request body is not valid JSON.", "Beautify Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void renderResponse(ApiResponse response) {
        lastApiResponseBody = response.rawBody;
        prettyResponseArea.setText(response.prettyBody);
        rawResponseArea.setText(response.rawBody);
        responseHeadersArea.setText(response.headersText);
        responseCookiesArea.setText(response.cookiesText.isBlank() ? "No cookies returned." : response.cookiesText);
        highlightJson(prettyResponseArea, response.prettyBody);

        statusValueLabel.setText(response.statusLine);
        statusValueLabel.setForeground(response.statusCode >= 200 && response.statusCode < 400 ? SUCCESS : new Color(196, 70, 54));
        timeValueLabel.setText(response.timeMs + " ms");
        sizeValueLabel.setText(response.sizeBytes + " bytes");
        parseResponseFieldsAsync(response.rawBody);
    }

    private void renderPerformanceResult(PerformanceTestResult result) {
        lastPerformanceReportPath = result.reportIndexPath;
        openReportButton.setEnabled(true);
        performanceSamplesLabel.setText(String.format("Samples: %d", result.samples));
        performanceErrorsLabel.setText(String.format("Errors: %d (%.2f%%)", result.errors, result.errorPercent));
        performanceThroughputLabel.setText(String.format("Throughput: %.2f req/s", result.throughputPerSecond));
        performanceDurationLabel.setText("Duration: " + formatDuration(result.duration));
        performanceReportLabel.setText("Report: " + result.reportIndexPath.getFileName());
        performanceChartPanel.updateValues(result.chartValuesMs);
        appendPerformanceLog("Latency summary:");
        appendPerformanceLog("Min: " + formatDuration(result.min)
                + " | Avg: " + formatDuration(result.mean)
                + " | Median: " + formatDuration(result.median));
        appendPerformanceLog("P90: " + formatDuration(result.perc90)
                + " | P95: " + formatDuration(result.perc95)
                + " | P99: " + formatDuration(result.perc99)
                + " | Max: " + formatDuration(result.max));
        if (result.requestCaptureJsonPath != null) {
            appendPerformanceLog("Per-hit API requests were captured in JSON at: "
                    + result.requestCaptureJsonPath);
        } else if (result.threads < 3 && result.iterationsPerThread < 5) {
            appendPerformanceLog("Per-hit API request capture could not be generated for this run, "
                    + "but the main HTML performance report was created successfully.");
        }
    }

    private void renderError(String message) {
        lastApiResponseBody = null;
        if (responseFieldsTableModel != null) {
            responseFieldsTableModel.setRowCount(0);
        }
        if (responseFieldsStatusLabel != null) {
            responseFieldsStatusLabel.setText("No JSON response fields available.");
        }
        if (fieldValidationsTableModel != null) {
            fieldValidationsTableModel.setRowCount(0);
        }
        if (fieldValidationsStatusLabel != null) {
            fieldValidationsStatusLabel.setText("No JSON response fields available.");
        }
        prettyResponseArea.setText(message);
        rawResponseArea.setText(message);
        responseHeadersArea.setText("");
        responseCookiesArea.setText("");
        statusValueLabel.setText("Failed");
        statusValueLabel.setForeground(new Color(196, 70, 54));
        timeValueLabel.setText("--");
        sizeValueLabel.setText("--");
    }

    private void copyResponse() {
        String text = prettyResponseArea.getText();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new java.awt.datatransfer.StringSelection(text), null
        );
    }

    private void parseResponseFieldsAsync(String responseBody) {
        if (responseFieldsTableModel == null && fieldValidationsTableModel == null) {
            return;
        }
        if (responseFieldsStatusLabel != null) {
            responseFieldsStatusLabel.setText("Parsing response fields in background...");
            responseFieldsStatusLabel.setForeground(PRIMARY);
            responseFieldsTableModel.setRowCount(0);
        }
        if (fieldValidationsStatusLabel != null) {
            fieldValidationsStatusLabel.setText("Parsing response fields in background...");
            fieldValidationsStatusLabel.setForeground(PRIMARY);
            fieldValidationsTableModel.setRowCount(0);
        }

        SwingWorker<List<ResponseFieldCandidate>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ResponseFieldCandidate> doInBackground() {
                return responseVariableService.parseAllFields(responseBody);
            }

            @Override
            protected void done() {
                try {
                    List<ResponseFieldCandidate> fields = get();
                    for (ResponseFieldCandidate field : fields) {
                        if (responseFieldsTableModel != null && isScalarJsonType(field.type)) {
                            responseFieldsTableModel.addRow(new Object[]{
                                    false,
                                    field.jsonPath,
                                    field.previewValue,
                                    defaultVariableName(field),
                                    field.type,
                                    field.value
                            });
                        }
                        if (fieldValidationsTableModel != null) {
                            fieldValidationsTableModel.addRow(new Object[]{
                                    false,
                                    field.jsonPath,
                                    field.previewValue,
                                    "Not Null",
                                    field.type,
                                    "",
                                    "Not run",
                                    field.value,
                                    field.type
                            });
                        }
                    }
                    long scalarCount = fields.stream().filter(field -> isScalarJsonType(field.type)).count();
                    if (responseFieldsStatusLabel != null) {
                        responseFieldsStatusLabel.setText(scalarCount == 0
                                ? "No scalar JSON fields found to capture."
                                : "Select response fields to save as variables.");
                        responseFieldsStatusLabel.setForeground(scalarCount == 0 ? new Color(95, 103, 120) : SUCCESS);
                    }
                    if (fieldValidationsStatusLabel != null) {
                        fieldValidationsStatusLabel.setText(fields.isEmpty()
                                ? "No JSON fields found to validate."
                                : "Configure null, type, and expected-value checks for response fields.");
                        fieldValidationsStatusLabel.setForeground(fields.isEmpty() ? new Color(95, 103, 120) : SUCCESS);
                    }
                } catch (Exception e) {
                    if (responseFieldsStatusLabel != null) {
                        responseFieldsStatusLabel.setText("Response is not valid JSON or could not be parsed.");
                        responseFieldsStatusLabel.setForeground(new Color(196, 70, 54));
                    }
                    if (fieldValidationsStatusLabel != null) {
                        fieldValidationsStatusLabel.setText("Response is not valid JSON or could not be parsed.");
                        fieldValidationsStatusLabel.setForeground(new Color(196, 70, 54));
                    }
                }
            }
        };
        worker.execute();
    }

    private void setAllResponseFieldSelections(boolean selected) {
        if (responseFieldsTableModel == null) {
            return;
        }
        for (int row = 0; row < responseFieldsTableModel.getRowCount(); row++) {
            responseFieldsTableModel.setValueAt(selected, row, 0);
        }
    }

    private String[] jsonTypeValidationOptions() {
        return new String[]{"Skip", "string", "number", "integer", "boolean", "object", "array", "null"};
    }

    private boolean isScalarJsonType(String type) {
        return "string".equals(type) || "number".equals(type) || "integer".equals(type)
                || "boolean".equals(type) || "null".equals(type);
    }

    private void resetFieldValidationDefaults() {
        if (fieldValidationsTableModel == null) {
            return;
        }
        stopFieldValidationEditing();
        for (int row = 0; row < fieldValidationsTableModel.getRowCount(); row++) {
            String type = stringCellValue(fieldValidationsTableModel, row, 8);
            fieldValidationsTableModel.setValueAt("Not Null", row, 3);
            fieldValidationsTableModel.setValueAt(type.isBlank() ? "Skip" : type, row, 4);
            fieldValidationsTableModel.setValueAt("", row, 5);
            fieldValidationsTableModel.setValueAt("Not run", row, 6);
        }
        if (fieldValidationsStatusLabel != null) {
            fieldValidationsStatusLabel.setText("Field validation defaults restored.");
            fieldValidationsStatusLabel.setForeground(new Color(95, 103, 120));
        }
    }

    private void runFieldValidations() {
        if (fieldValidationsTableModel == null || fieldValidationsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No response fields are available to validate.",
                    "Field Validations", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stopFieldValidationEditing();
        int passed = 0;
        int failed = 0;
        for (int row = 0; row < fieldValidationsTableModel.getRowCount(); row++) {
            String actualType = stringCellValue(fieldValidationsTableModel, row, 8);
            String actualValue = stringCellValue(fieldValidationsTableModel, row, 7);
            String nullRule = stringCellValue(fieldValidationsTableModel, row, 3);
            String typeRule = stringCellValue(fieldValidationsTableModel, row, 4);
            String expectedValue = stringCellValue(fieldValidationsTableModel, row, 5);

            List<String> errors = new ArrayList<>();
            if ("Not Null".equals(nullRule) && "null".equals(actualType)) {
                errors.add("expected not null");
            } else if ("Null".equals(nullRule) && !"null".equals(actualType)) {
                errors.add("expected null");
            }
            if (!typeRule.isBlank() && !"Skip".equals(typeRule) && !typeRule.equals(actualType)) {
                if (!("number".equals(typeRule) && "integer".equals(actualType))) {
                    errors.add("expected " + typeRule);
                }
            }
            if (!expectedValue.isBlank()) {
                String resolvedExpected = resolveVariablesInText(expectedValue);
                if (!resolvedExpected.equals(actualValue)) {
                    errors.add("expected value mismatch");
                }
            }

            if (errors.isEmpty()) {
                fieldValidationsTableModel.setValueAt("Passed", row, 6);
                passed++;
            } else {
                fieldValidationsTableModel.setValueAt("Failed: " + String.join(", ", errors), row, 6);
                failed++;
            }
        }
        if (fieldValidationsStatusLabel != null) {
            fieldValidationsStatusLabel.setText("Field validation complete. Passed: " + passed + "  Failed: " + failed);
            fieldValidationsStatusLabel.setForeground(failed == 0 ? SUCCESS : new Color(196, 70, 54));
        }
    }

    private void stopFieldValidationEditing() {
        if (fieldValidationsTable != null && fieldValidationsTable.isEditing()) {
            fieldValidationsTable.getCellEditor().stopCellEditing();
        }
    }

    private void addFieldValidationsToTestRunner() {
        if (fieldValidationsTableModel == null || fieldValidationsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No field validations are available to add.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }
        stopFieldValidationEditing();
        if (responseFieldsTable != null && responseFieldsTable.isEditing()) {
            responseFieldsTable.getCellEditor().stopCellEditing();
        }

        List<Map<String, String>> selectedValidations = selectedFieldValidationSteps();
        if (selectedValidations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one field validation row using the Add checkbox.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path workbookPath = currentTestSuiteWorkbookPath();
        if (workbookPath == null) {
            JOptionPane.showMessageDialog(this, "Import or create a Test Suite Runner workbook first.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ValidationSummary summary = validateSelectedFieldValidationRows();
            selectedValidations = selectedFieldValidationSteps();
            JSONObject validationStep = buildApiFieldValidationStepJson(selectedValidations);
            appendRowsToWorkbook(workbookPath, List.of(List.of(
                    currentText(fieldValidationTestSuiteField),
                    currentText(fieldValidationTestCaseField),
                    currentTestStepName(),
                    buildRunnerRequestJson().toString(),
                    runnerRequestPayload(),
                    buildSelectedCaptureVariablesJson().toString(),
                    validationStep.toString(),
                    buildVariableDependenciesJson(selectedValidations).toString(),
                    "",
                    ""
            )));
            testSuiteWorkbookPathField.setText(workbookPath.toAbsolutePath().toString());
            testSuiteWorkbookPathField.setForeground(new Color(35, 44, 58));
            fieldValidationsStatusLabel.setText("Added " + selectedValidations.size()
                    + " field validation(s) to Test Runner. Passed: " + summary.passed + "  Failed: " + summary.failed);
            fieldValidationsStatusLabel.setForeground(summary.failed == 0 ? SUCCESS : new Color(196, 70, 54));
            refreshTestSuiteRunnerSteps(workbookPath);
            JOptionPane.showMessageDialog(this,
                    "Selected field validations were added to the Test Suite Runner workbook.",
                    "Test Runner", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            JOptionPane.showMessageDialog(this, message, "Add to Test Runner Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addJsonCompareToTestRunner() {
        String expectedPath = filePathField == null ? "" : filePathField.getText().trim();
        if (expectedPath.isBlank()) {
            JOptionPane.showMessageDialog(this, "Select an Expected JSON file before adding JSON Compare to Test Runner.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path expectedFile = Path.of(expectedPath);
        if (!Files.exists(expectedFile)) {
            JOptionPane.showMessageDialog(this, "Expected JSON file does not exist:\n" + expectedPath,
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path workbookPath = currentTestSuiteWorkbookPath();
        if (workbookPath == null) {
            JOptionPane.showMessageDialog(this, "Import or create a Test Suite Runner workbook first.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            appendRowsToWorkbook(workbookPath, List.of(List.of(
                    currentText(jsonCompareTestSuiteField),
                    currentText(jsonCompareTestCaseField),
                    currentJsonCompareTestStepName(),
                    buildRunnerRequestJson().toString(),
                    runnerRequestPayload(),
                    buildSelectedCaptureVariablesJson().toString(),
                    "",
                    buildVariableDependenciesJson(List.of()).toString(),
                    buildJsonCompareStepJson(workbookPath, expectedFile).toString(),
                    ""
            )));
            testSuiteWorkbookPathField.setText(workbookPath.toAbsolutePath().toString());
            testSuiteWorkbookPathField.setForeground(new Color(35, 44, 58));
            refreshTestSuiteRunnerSteps(workbookPath);
            JOptionPane.showMessageDialog(this,
                    "JSON Compare test step was added to the Test Suite Runner workbook.",
                    "Test Runner", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            JOptionPane.showMessageDialog(this, message, "Add to Test Runner Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDbValidationsToTestRunner() {
        stopDbRuleEditing();
        stopDbColumnValidationEditing();

        List<Map<String, String>> apiDbValidations = selectedApiDbValidationSteps();
        List<Map<String, String>> dbColumnValidations = selectedDbColumnValidationSteps();
        if (apiDbValidations.isEmpty() && dbColumnValidations.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Check at least one validation row in API-DB Validation or DB Validation before adding to Test Runner.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sqlQuery = dbQueryArea == null ? "" : dbQueryArea.getText().trim();
        if (sqlQuery.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter the SQL query before adding DB validations to Test Runner.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dbConnectionFilePath == null || !Files.exists(dbConnectionFilePath)) {
            JOptionPane.showMessageDialog(this,
                    "Save or load the DB connection JSON file first so the Test Suite Runner can reuse it later.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path workbookPath = currentTestSuiteWorkbookPath();
        if (workbookPath == null) {
            JOptionPane.showMessageDialog(this, "Import or create a Test Suite Runner workbook first.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            JSONObject dbConnection = buildDbConnectionJson(workbookPath);
            appendRowsToWorkbook(workbookPath, List.of(List.of(
                    currentText(dbValidationTestSuiteField),
                    currentText(dbValidationTestCaseField),
                    currentDbValidationTestStepName(),
                    buildRunnerRequestJson().toString(),
                    runnerRequestPayload(),
                    buildSelectedCaptureVariablesJson().toString(),
                    "",
                    buildDbVariableDependenciesJson(apiDbValidations, dbColumnValidations).toString(),
                    "",
                    "",
                    dbConnection.toString(),
                    dbQueryArea.getText(),
                    new JSONArray(apiDbValidations).toString(),
                    new JSONArray(dbColumnValidations).toString()
            )));
            testSuiteWorkbookPathField.setText(workbookPath.toAbsolutePath().toString());
            testSuiteWorkbookPathField.setForeground(new Color(35, 44, 58));
            if (dbColumnValidationsStatusLabel != null) {
                dbColumnValidationsStatusLabel.setText("Added " + apiDbValidations.size() + " API-DB rule(s) and "
                        + dbColumnValidations.size() + " DB column validation(s) to Test Runner.");
                dbColumnValidationsStatusLabel.setForeground(SUCCESS);
            }
            refreshTestSuiteRunnerSteps(workbookPath);
            JOptionPane.showMessageDialog(this,
                    "Selected DB validations were added to the Test Suite Runner workbook.",
                    "Test Runner", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            JOptionPane.showMessageDialog(this, message, "Add to Test Runner Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addWebTestToTestRunner() {
        if (webStepsTable != null && webStepsTable.isEditing()) {
            webStepsTable.getCellEditor().stopCellEditing();
        }
        WebTestCase testCase = buildCurrentRawWebTestCase();
        if (testCase.steps.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Record, import, or add at least one web step before adding to Test Runner.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path workbookPath = currentTestSuiteWorkbookPath();
        if (workbookPath == null) {
            JOptionPane.showMessageDialog(this, "Import or create a Test Suite Runner workbook first.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            appendRowsToWorkbook(workbookPath, List.of(List.of(
                    currentText(webTestingTestSuiteField),
                    currentText(webTestingTestCaseField),
                    currentWebTestStepName(),
                    "",
                    "",
                    "",
                    "",
                    buildWebVariableDependenciesJson(testCase).toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    buildWebTestStepJson(testCase).toString()
            )));
            testSuiteWorkbookPathField.setText(workbookPath.toAbsolutePath().toString());
            testSuiteWorkbookPathField.setForeground(new Color(35, 44, 58));
            webRecorderStatusLabel.setText("Added " + testCase.steps.size() + " web step(s) to Test Runner.");
            webRecorderStatusLabel.setForeground(SUCCESS);
            refreshTestSuiteRunnerSteps(workbookPath);
            JOptionPane.showMessageDialog(this,
                    "Recorded/imported web steps were added to the Test Suite Runner workbook.",
                    "Test Runner", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            JOptionPane.showMessageDialog(this, message, "Add to Test Runner Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPerformanceTestToTestRunner() {
        String endpoint = endpointField == null ? "" : endpointField.getText().trim();
        if (endpoint.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter an endpoint in API Tester before adding Performance Test to Test Runner.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path workbookPath = currentTestSuiteWorkbookPath();
        if (workbookPath == null) {
            JOptionPane.showMessageDialog(this, "Import or create a Test Suite Runner workbook first.",
                    "Test Runner", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            appendRowsToWorkbook(workbookPath, List.of(List.of(
                    currentText(performanceTestSuiteField),
                    currentText(performanceTestCaseField),
                    currentPerformanceTestStepName(),
                    buildRunnerRequestJson().toString(),
                    performanceRunnerRequestPayload(),
                    "",
                    "",
                    buildPerformanceVariableDependenciesJson().toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    buildPerformanceTestStepJson().toString()
            )));
            testSuiteWorkbookPathField.setText(workbookPath.toAbsolutePath().toString());
            testSuiteWorkbookPathField.setForeground(new Color(35, 44, 58));
            performanceConfigLabel.setText("Added Performance Test to Test Runner: "
                    + currentPerformanceTestStepName());
            performanceConfigLabel.setForeground(SUCCESS);
            refreshTestSuiteRunnerSteps(workbookPath);
            JOptionPane.showMessageDialog(this,
                    "Performance Test was added to the Test Suite Runner workbook.",
                    "Test Runner", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            JOptionPane.showMessageDialog(this, message, "Add to Test Runner Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String currentWebTestStepName() {
        String testStep = currentText(webTestingTestStepField);
        if (!testStep.isBlank()) {
            return testStep;
        }
        String testCase = currentText(webTestingTestCaseField);
        return testCase.isBlank() ? "Web Test" : testCase + " Web Test";
    }

    private String currentPerformanceTestStepName() {
        String testStep = currentText(performanceTestStepField);
        if (!testStep.isBlank()) {
            return testStep;
        }
        String testCase = currentText(performanceTestCaseField);
        return testCase.isBlank() ? "Performance Test" : testCase + " Performance Test";
    }

    private JSONObject buildPerformanceTestStepJson() {
        JSONObject step = new JSONObject();
        step.put("stepType", "PERFORMANCE_TEST");
        step.put("threads", ((Number) performanceThreadsSpinner.getValue()).intValue());
        step.put("iterationsPerThread", ((Number) performanceIterationsSpinner.getValue()).intValue());
        step.put("requestBodySource", "Performance Test");
        return step;
    }

    private String performanceRunnerRequestPayload() {
        return isBodyAllowedForSelectedMethod() ? performanceBodyArea.getText().trim() : "";
    }

    private JSONObject buildPerformanceVariableDependenciesJson() {
        return buildVariableDependenciesJson(List.of(), performanceBodyArea.getText());
    }

    private JSONObject buildWebTestStepJson(WebTestCase testCase) {
        JSONObject webTest = new JSONObject();
        webTest.put("stepType", "WEB_TEST");
        webTest.put("testName", testCase.testName);
        webTest.put("startUrl", testCase.startUrl);
        webTest.put("headless", webHeadlessCheckbox == null || webHeadlessCheckbox.isSelected());
        webTest.put("slowMoMillis", webSlowMoCheckbox != null && webSlowMoCheckbox.isSelected() ? 250 : 0);
        JSONArray steps = new JSONArray();
        for (WebTestStep step : testCase.steps) {
            JSONObject stepJson = new JSONObject();
            stepJson.put("action", step.action);
            stepJson.put("selector", step.selector);
            stepJson.put("value", step.value);
            stepJson.put("note", step.note);
            stepJson.put("suggested", step.suggested);
            steps.put(stepJson);
        }
        webTest.put("steps", steps);
        return webTest;
    }

    private JSONObject buildWebVariableDependenciesJson(WebTestCase testCase) {
        List<String> webTexts = new ArrayList<>();
        webTexts.add(testCase.testName);
        webTexts.add(testCase.startUrl);
        for (WebTestStep step : testCase.steps) {
            webTexts.add(step.selector);
            webTexts.add(step.value);
            webTexts.add(step.note);
        }
        return buildVariableDependenciesJson(List.of(), webTexts.toArray(new String[0]));
    }

    private String currentJsonCompareTestStepName() {
        String testStep = currentText(jsonCompareTestStepField);
        if (!testStep.isBlank()) {
            return testStep;
        }
        String testCase = currentText(jsonCompareTestCaseField);
        return testCase.isBlank() ? "JSON Compare" : testCase + " JSON Compare";
    }

    private String currentDbValidationTestStepName() {
        String testStep = currentText(dbValidationTestStepField);
        if (!testStep.isBlank()) {
            return testStep;
        }
        String testCase = currentText(dbValidationTestCaseField);
        return testCase.isBlank() ? "DB Validation" : testCase + " DB Validation";
    }

    private JSONObject buildJsonCompareStepJson(Path workbookPath, Path expectedFile) {
        JSONObject step = new JSONObject();
        step.put("stepType", "JSON_COMPARE");
        step.put("compareMode", String.valueOf(compareModeDropdown.getSelectedItem()));
        step.put("expectedResponse", new JSONObject(Map.of(
                "path", expectedFile.toAbsolutePath().toString(),
                "relativePath", relativePathFromWorkbook(workbookPath, expectedFile),
                "encoding", StandardCharsets.UTF_8.name()
        )));
        return step;
    }

    private JSONObject buildDbConnectionJson(Path workbookPath) {
        return new JSONObject(Map.of(
                "path", dbConnectionFilePath.toString(),
                "relativePath", relativePathFromWorkbook(workbookPath, dbConnectionFilePath),
                "databaseType", String.valueOf(dbTypeDropdown.getSelectedItem()),
                "jdbcUrl", jdbcUrlField.getText().trim(),
                "username", dbUsernameField.getText().trim(),
                "driverClass", driverClassField.getText().trim()
        ));
    }

    private String relativePathFromWorkbook(Path workbookPath, Path targetPath) {
        try {
            Path workbookDirectory = workbookPath.toAbsolutePath().getParent();
            if (workbookDirectory == null) {
                return targetPath.getFileName().toString();
            }
            return workbookDirectory.relativize(targetPath.toAbsolutePath()).toString();
        } catch (Exception ignored) {
            return targetPath.getFileName().toString();
        }
    }

    private ValidationSummary validateSelectedFieldValidationRows() {
        int passed = 0;
        int failed = 0;
        for (int row = 0; row < fieldValidationsTableModel.getRowCount(); row++) {
            Object value = fieldValidationsTableModel.getValueAt(row, 0);
            if (!(value instanceof Boolean selectedRow) || !selectedRow) {
                continue;
            }

            String actualType = stringCellValue(fieldValidationsTableModel, row, 8);
            String actualValue = stringCellValue(fieldValidationsTableModel, row, 7);
            String nullRule = stringCellValue(fieldValidationsTableModel, row, 3);
            String typeRule = stringCellValue(fieldValidationsTableModel, row, 4);
            String expectedValue = stringCellValue(fieldValidationsTableModel, row, 5);

            List<String> errors = fieldValidationErrors(actualType, actualValue, nullRule, typeRule, expectedValue);
            if (errors.isEmpty()) {
                fieldValidationsTableModel.setValueAt("Passed", row, 6);
                passed++;
            } else {
                fieldValidationsTableModel.setValueAt("Failed: " + String.join(", ", errors), row, 6);
                failed++;
            }
        }
        return new ValidationSummary(passed, failed);
    }

    private List<String> fieldValidationErrors(String actualType, String actualValue,
                                               String nullRule, String typeRule, String expectedValue) {
        List<String> errors = new ArrayList<>();
        if ("Not Null".equals(nullRule) && "null".equals(actualType)) {
            errors.add("expected not null");
        } else if ("Null".equals(nullRule) && !"null".equals(actualType)) {
            errors.add("expected null");
        }
        if (!typeRule.isBlank() && !"Skip".equals(typeRule) && !typeRule.equals(actualType)) {
            if (!("number".equals(typeRule) && "integer".equals(actualType))) {
                errors.add("expected " + typeRule);
            }
        }
        if (!expectedValue.isBlank()) {
            String resolvedExpected = resolveVariablesInText(expectedValue);
            if (!resolvedExpected.equals(actualValue)) {
                errors.add("expected value mismatch");
            }
        }
        return errors;
    }

    private record ValidationSummary(int passed, int failed) {
    }

    private record RunnerIndexedStepReport(int index, RunnerStepReport report) {
    }

    private static class RunnerValidationReport {
        String field;
        String validation;
        String expected;
        String actual;
        boolean passed;
        String message;
    }

    private static class RunnerStepReport {
        String testSuite;
        String testCase;
        String testStep;
        String stepType;
        int statusCode;
        long timeMs;
        boolean passed;
        Path performanceReportPath;
        final List<RunnerValidationReport> validations = new ArrayList<>();

        void addValidation(String field, String validation, String expected, String actual, boolean passed, String message) {
            RunnerValidationReport report = new RunnerValidationReport();
            report.field = field == null ? "" : field;
            report.validation = validation == null ? "" : validation;
            report.expected = expected == null ? "" : expected;
            report.actual = actual == null ? "" : actual;
            report.passed = passed;
            report.message = message == null ? "" : message;
            validations.add(report);
        }
    }

    private String buildTestSuiteReportHtml(Path workbookPath, List<RunnerStepReport> reports, int passed, int failed) {
        int total = passed + failed;
        int passPercent = total == 0 ? 0 : Math.round((passed * 100f) / total);
        int failPercent = total == 0 ? 0 : 100 - passPercent;
        Map<String, Map<String, List<Integer>>> tree = new LinkedHashMap<>();
        for (int i = 0; i < reports.size(); i++) {
            RunnerStepReport report = reports.get(i);
            String suite = report.testSuite == null || report.testSuite.isBlank() ? "Untitled Suite" : report.testSuite;
            String testCase = report.testCase == null || report.testCase.isBlank() ? "Untitled Test Case" : report.testCase;
            tree.computeIfAbsent(suite, key -> new LinkedHashMap<>())
                    .computeIfAbsent(testCase, key -> new ArrayList<>())
                    .add(i);
        }

        StringBuilder html = new StringBuilder();
        html.append("""
                <!doctype html>
                <html>
                <head>
                  <meta charset="utf-8">
                  <title>TestWeave Test Suite Report</title>
                  <style>
                    :root{--blue:#1e5ed6;--ink:#172033;--muted:#5f6778;--line:#d2dceb;--bg:#f5f7fb;--panel:#fff;--pass:#12864a;--fail:#c44636}
                    *{box-sizing:border-box}
                    body{font-family:Segoe UI,Arial,sans-serif;margin:0;background:var(--bg);color:var(--ink)}
                    header{background:#164da8;color:white;padding:22px 30px}
                    header h1{margin:0 0 6px;font-size:26px}
                    header div{opacity:.9;font-size:13px;word-break:break-all}
                    main{display:grid;grid-template-columns:310px minmax(0,1fr);gap:18px;padding:18px 24px 28px}
                    aside{background:var(--panel);border:1px solid var(--line);border-radius:8px;min-height:calc(100vh - 130px);padding:14px;position:sticky;top:14px;align-self:start}
                    aside h2{margin:0 0 12px;color:var(--blue);font-size:18px}
                    details{border-top:1px solid #eef2f8;padding:8px 0}
                    summary{cursor:pointer;font-weight:700;color:#22314d}
                    .case summary{font-weight:600;color:#44506a;margin-left:10px}
                    .step-link{display:flex;align-items:center;gap:8px;width:calc(100% - 22px);margin:6px 0 4px 22px;padding:8px 9px;border:1px solid transparent;border-radius:6px;background:white;color:#26344f;text-align:left;cursor:pointer;font:13px Segoe UI,Arial,sans-serif}
                    .step-link:hover,.step-link.active{border-color:#9db8e9;background:#eef5ff}
                    .dot{width:9px;height:9px;border-radius:50%;display:inline-block;flex:0 0 auto}.dot.pass{background:var(--pass)}.dot.fail{background:var(--fail)}
                    .summary-cards{display:grid;grid-template-columns:repeat(4,minmax(140px,1fr));gap:12px;margin-bottom:14px}
                    .metric{background:var(--panel);border:1px solid var(--line);padding:14px;border-radius:8px}
                    .metric b{display:block;font-size:28px;line-height:1.1}
                    .viz{display:grid;grid-template-columns:260px minmax(0,1fr);gap:14px;margin-bottom:14px}
                    .card{background:var(--panel);border:1px solid var(--line);border-radius:8px;padding:16px}
                    .pie{width:164px;height:164px;border-radius:50%;margin:8px auto;background:conic-gradient(var(--pass) 0 var(--pass-pct),var(--fail) var(--pass-pct) 100%)}
                    .bar{height:28px;display:flex;border-radius:5px;overflow:hidden;background:#e9eef7;margin:20px 0 10px}.passbar{background:var(--pass)}.failbar{background:var(--fail)}
                    .legend{color:var(--muted);font-size:14px}
                    .detail{display:none;background:var(--panel);border:1px solid var(--line);border-radius:8px;padding:18px}
                    .detail.active{display:block}
                    .detail-head{display:flex;justify-content:space-between;gap:16px;border-bottom:1px solid #e6edf7;padding-bottom:12px;margin-bottom:14px}
                    h2{margin:0;color:var(--blue);font-size:22px}.meta{color:var(--muted);font-size:14px;margin-top:6px}
                    .pill{border-radius:999px;padding:6px 10px;font-weight:700;align-self:start}.pill.pass{background:#e8f6ef;color:var(--pass)}.pill.fail{background:#fdecea;color:var(--fail)}
                    .facts{display:grid;grid-template-columns:repeat(4,minmax(120px,1fr));gap:10px;margin-bottom:14px}
                    .fact{background:#f8fbff;border:1px solid #e4ebf6;border-radius:6px;padding:10px}.fact span{display:block;color:var(--muted);font-size:12px}.fact b{font-size:15px}
                    .error{background:#fff5f3;border:1px solid #f0bbb2;color:#8f2f22;border-radius:6px;padding:12px;margin:12px 0}
                    table{width:100%;border-collapse:collapse;font-size:14px}
                    th,td{border:1px solid #d9e2f1;padding:8px;vertical-align:top;text-align:left}
                    th{background:#eef3fb}.ok{color:var(--pass);font-weight:700}.bad{color:var(--fail);font-weight:700}
                    pre{white-space:pre-wrap;margin:0;font-family:Consolas,monospace;font-size:13px}
                    a{color:var(--blue);font-weight:600}
                    @media(max-width:960px){main{grid-template-columns:1fr}aside{position:static;min-height:auto}.summary-cards,.viz,.facts{grid-template-columns:1fr}}
                  </style>
                </head>
                <body>
                """);
        html.append("<header><h1>Test Suite Run Report</h1><div>")
                .append(escapeXml(workbookPath.toAbsolutePath().toString()))
                .append("</div></header><main>");

        html.append("<aside><h2>Execution Tree</h2>");
        for (Map.Entry<String, Map<String, List<Integer>>> suiteEntry : tree.entrySet()) {
            html.append("<details open><summary>").append(escapeXml(suiteEntry.getKey())).append("</summary>");
            for (Map.Entry<String, List<Integer>> caseEntry : suiteEntry.getValue().entrySet()) {
                html.append("<details class=\"case\" open><summary>").append(escapeXml(caseEntry.getKey())).append("</summary>");
                for (Integer reportIndex : caseEntry.getValue()) {
                    RunnerStepReport report = reports.get(reportIndex);
                    html.append("<button class=\"step-link")
                            .append(reportIndex == 0 ? " active" : "")
                            .append("\" data-step=\"step-").append(reportIndex).append("\"><span class=\"dot ")
                            .append(report.passed ? "pass" : "fail")
                            .append("\"></span><span>")
                            .append(escapeXml(report.testStep))
                            .append("</span></button>");
                }
                html.append("</details>");
            }
            html.append("</details>");
        }
        html.append("</aside><section>");

        html.append("<div class=\"summary-cards\">")
                .append(metricHtml("Total Steps", String.valueOf(total)))
                .append(metricHtml("Passed", String.valueOf(passed)))
                .append(metricHtml("Failed", String.valueOf(failed)))
                .append(metricHtml("Pass Rate", passPercent + "%"))
                .append("</div>");
        html.append("<div class=\"viz\"><div class=\"card\"><h2>Status Split</h2><div class=\"pie\" style=\"--pass-pct:")
                .append(passPercent)
                .append("%\"></div><div class=\"legend\">Passed: ")
                .append(passed)
                .append(" | Failed: ")
                .append(failed)
                .append("</div></div><div class=\"card\"><h2>Run Health</h2><div class=\"bar\">")
                .append("<div class=\"passbar\" style=\"width:").append(passPercent).append("%\"></div>")
                .append("<div class=\"failbar\" style=\"width:").append(failPercent).append("%\"></div>")
                .append("</div><div class=\"legend\">Use the execution tree to inspect every test step, validations, expected values, actual values, and error messages.</div></div></div>");

        for (int i = 0; i < reports.size(); i++) {
            RunnerStepReport report = reports.get(i);
            String firstFailure = report.validations.stream()
                    .filter(validation -> !validation.passed)
                    .map(validation -> validation.message)
                    .filter(message -> message != null && !message.isBlank())
                    .findFirst()
                    .orElse("");
            html.append("<article id=\"step-").append(i).append("\" class=\"detail")
                    .append(i == 0 ? " active" : "")
                    .append("\"><div class=\"detail-head\"><div><h2>")
                    .append(escapeXml(report.testStep))
                    .append("</h2><div class=\"meta\">")
                    .append(escapeXml(report.testSuite)).append(" / ")
                    .append(escapeXml(report.testCase))
                    .append("</div></div><span class=\"pill ")
                    .append(report.passed ? "pass\">PASS" : "fail\">FAIL")
                    .append("</span></div>");
            html.append("<div class=\"facts\"><div class=\"fact\"><span>Step Type</span><b>")
                    .append(escapeXml(report.stepType))
                    .append("</b></div><div class=\"fact\"><span>HTTP Status</span><b>")
                    .append(report.statusCode)
                    .append("</b></div><div class=\"fact\"><span>Duration</span><b>")
                    .append(report.timeMs)
                    .append(" ms</b></div><div class=\"fact\"><span>Validations</span><b>")
                    .append(report.validations.size())
                    .append("</b></div></div>");
            if (!firstFailure.isBlank()) {
                html.append("<div class=\"error\"><b>Failure Error Message</b><br>")
                        .append(escapeXml(firstFailure))
                        .append("</div>");
            }
            if (report.performanceReportPath != null) {
                html.append("<p><a href=\"")
                        .append(escapeXml(report.performanceReportPath.toUri().toString()))
                        .append("\" target=\"_blank\">Open executed Performance Test Report</a></p>");
            }
            html.append("<table><thead><tr><th>Status</th><th>Field</th><th>Validation</th><th>Expected</th><th>Actual</th><th>Message</th></tr></thead><tbody>");
            for (RunnerValidationReport validation : report.validations) {
                html.append("<tr><td>")
                        .append(validation.passed ? "<span class=\"ok\">PASS</span>" : "<span class=\"bad\">FAIL</span>")
                        .append("</td><td>").append(escapeXml(validation.field))
                        .append("</td><td>").append(escapeXml(validation.validation))
                        .append("</td><td><pre>").append(escapeXml(validation.expected))
                        .append("</pre></td><td><pre>").append(escapeXml(validation.actual))
                        .append("</pre></td><td>").append(escapeXml(validation.message))
                        .append("</td></tr>");
            }
            html.append("</tbody></table></article>");
        }
        html.append("</section></main><script>")
                .append("document.querySelectorAll('.step-link').forEach(function(btn){btn.addEventListener('click',function(){")
                .append("document.querySelectorAll('.step-link').forEach(function(item){item.classList.remove('active')});")
                .append("document.querySelectorAll('.detail').forEach(function(item){item.classList.remove('active')});")
                .append("btn.classList.add('active');var target=document.getElementById(btn.dataset.step);")
                .append("if(target){target.classList.add('active');target.scrollIntoView({behavior:'smooth',block:'start'});}")
                .append("});});")
                .append("</script></body></html>");
        return html.toString();
    }

    private String metricHtml(String label, String value) {
        return "<div class=\"metric\"><b>" + escapeXml(value) + "</b>" + escapeXml(label) + "</div>";
    }

    private List<Map<String, String>> selectedFieldValidationSteps() {
        List<Map<String, String>> selected = new ArrayList<>();
        for (int row = 0; row < fieldValidationsTableModel.getRowCount(); row++) {
            Object value = fieldValidationsTableModel.getValueAt(row, 0);
            if (!(value instanceof Boolean selectedRow) || !selectedRow) {
                continue;
            }
            Map<String, String> validation = new LinkedHashMap<>();
            validation.put("jsonPath", stringCellValue(fieldValidationsTableModel, row, 1));
            validation.put("previewValue", stringCellValue(fieldValidationsTableModel, row, 2));
            validation.put("nullValidation", stringCellValue(fieldValidationsTableModel, row, 3));
            validation.put("typeValidation", stringCellValue(fieldValidationsTableModel, row, 4));
            validation.put("expectedValueOrVariable", stringCellValue(fieldValidationsTableModel, row, 5));
            validation.put("result", stringCellValue(fieldValidationsTableModel, row, 6));
            validation.put("actualValue", stringCellValue(fieldValidationsTableModel, row, 7));
            validation.put("actualType", stringCellValue(fieldValidationsTableModel, row, 8));
            selected.add(validation);
        }
        return selected;
    }

    private List<Map<String, String>> selectedApiDbValidationSteps() {
        List<Map<String, String>> selected = new ArrayList<>();
        if (dbRulesTableModel == null) {
            return selected;
        }
        for (int row = 0; row < dbRulesTableModel.getRowCount(); row++) {
            if (!isRowChecked(dbRulesTableModel, row)) {
                continue;
            }
            String apiField = stringCellValue(dbRulesTableModel, row, 1);
            String dbColumn = stringCellValue(dbRulesTableModel, row, 2);
            String operator = stringCellValue(dbRulesTableModel, row, 3);
            String description = stringCellValue(dbRulesTableModel, row, 4);
            if (apiField.isBlank() && dbColumn.isBlank() && operator.isBlank() && description.isBlank()) {
                continue;
            }
            Map<String, String> validation = new LinkedHashMap<>();
            validation.put("apiField", apiField);
            validation.put("dbColumn", dbColumn);
            validation.put("operator", operator.isBlank() ? "=" : operator);
            validation.put("description", description);
            selected.add(validation);
        }
        return selected;
    }

    private List<Map<String, String>> selectedDbColumnValidationSteps() {
        List<Map<String, String>> selected = new ArrayList<>();
        if (dbColumnValidationsTableModel == null) {
            return selected;
        }
        for (int row = 0; row < dbColumnValidationsTableModel.getRowCount(); row++) {
            if (!isRowChecked(dbColumnValidationsTableModel, row)) {
                continue;
            }
            Map<String, String> validation = new LinkedHashMap<>();
            validation.put("dbColumnName", stringCellValue(dbColumnValidationsTableModel, row, 1));
            validation.put("previewValue", stringCellValue(dbColumnValidationsTableModel, row, 2));
            validation.put("nullValidation", stringCellValue(dbColumnValidationsTableModel, row, 3));
            validation.put("typeValidation", stringCellValue(dbColumnValidationsTableModel, row, 4));
            validation.put("expectedValueOrVariable", stringCellValue(dbColumnValidationsTableModel, row, 5));
            validation.put("result", stringCellValue(dbColumnValidationsTableModel, row, 6));
            validation.put("actualValue", stringCellValue(dbColumnValidationsTableModel, row, 7));
            validation.put("actualType", stringCellValue(dbColumnValidationsTableModel, row, 8));
            selected.add(validation);
        }
        return selected;
    }

    private String currentTestStepName() {
        String testStep = currentText(fieldValidationTestStepField);
        if (!testStep.isBlank()) {
            return testStep;
        }
        String testCase = currentText(fieldValidationTestCaseField);
        return testCase.isBlank() ? "API Field Validation" : testCase + " API Field Validation";
    }

    private JSONObject buildApiFieldValidationStepJson(List<Map<String, String>> selectedValidations) {
        JSONObject step = new JSONObject();
        step.put("stepType", "API_FIELD_VALIDATION");
        step.put("validations", new JSONArray(selectedValidations));
        return step;
    }

    private JSONObject buildRunnerRequestJson() {
        JSONObject request = new JSONObject();
        request.put("method", String.valueOf(methodDropdown.getSelectedItem()));
        request.put("endpoint", normalizeEndpointUrl(endpointField.getText().trim()));
        request.put("headersText", headersArea.getText());
        request.put("headers", new JSONObject(parseHeaders(headersArea.getText())));
        request.put("params", new JSONObject(parseQueryParams(endpointField.getText().trim())));
        request.put("authType", String.valueOf(authTypeDropdown.getSelectedItem()));
        request.put("authorization", new JSONObject(Map.of(
                "type", String.valueOf(authTypeDropdown.getSelectedItem()),
                "token", new String(tokenField.getPassword()).trim()
        )));
        request.put("requestFormat", String.valueOf(requestFormatDropdown.getSelectedItem()));
        return request;
    }

    private String runnerRequestPayload() {
        return isBodyAllowedForSelectedMethod() ? bodyArea.getText().trim() : "";
    }

    private JSONArray buildSelectedCaptureVariablesJson() {
        JSONArray captures = new JSONArray();
        if (responseFieldsTableModel == null) {
            return captures;
        }
        for (int row = 0; row < responseFieldsTableModel.getRowCount(); row++) {
            Object value = responseFieldsTableModel.getValueAt(row, 0);
            if (value instanceof Boolean selected && selected) {
                JSONObject capture = new JSONObject();
                capture.put("jsonPath", stringCellValue(responseFieldsTableModel, row, 1));
                capture.put("previewValue", stringCellValue(responseFieldsTableModel, row, 2));
                capture.put("variableName", stringCellValue(responseFieldsTableModel, row, 3));
                capture.put("type", stringCellValue(responseFieldsTableModel, row, 4));
                capture.put("value", stringCellValue(responseFieldsTableModel, row, 5));
                captures.put(capture);
            }
        }
        return captures;
    }

    private JSONObject buildVariableDependenciesJson(List<Map<String, String>> selectedValidations, String... extraTexts) {
        Set<String> names = new HashSet<>();
        collectVariableNames(endpointField.getText(), names);
        collectVariableNames(headersArea.getText(), names);
        collectVariableNames(bodyArea.getText(), names);
        collectVariableNames(new String(tokenField.getPassword()), names);
        for (Map<String, String> validation : selectedValidations) {
            collectVariableNames(validation.get("expectedValueOrVariable"), names);
        }
        if (extraTexts != null) {
            for (String extraText : extraTexts) {
                collectVariableNames(extraText, names);
            }
        }

        JSONObject dependencies = new JSONObject();
        for (String name : names) {
            JSONObject dependency = new JSONObject();
            if (RUNTIME_VARIABLES.contains(name)) {
                dependency.put("source", "runtime");
                dependency.put("generator", name);
                dependency.put("value", "");
                dependency.put("jsonPath", "");
                dependency.put("type", runtimeVariableType(name));
                dependency.put("resolved", true);
            } else {
                dependency.put("source", "saved");
                dependency.put("value", savedVariables.getOrDefault(name, ""));
                dependency.put("jsonPath", savedVariablePaths.getOrDefault(name, ""));
                dependency.put("type", savedVariableTypes.getOrDefault(name, ""));
                dependency.put("resolved", savedVariables.containsKey(name));
            }
            dependencies.put(name, dependency);
        }
        return dependencies;
    }

    private JSONObject buildDbVariableDependenciesJson(List<Map<String, String>> apiDbValidations,
                                                       List<Map<String, String>> dbColumnValidations) {
        List<String> dbTexts = new ArrayList<>();
        dbTexts.add(dbQueryArea.getText());
        for (Map<String, String> validation : apiDbValidations) {
            dbTexts.add(validation.get("apiField"));
            dbTexts.add(validation.get("dbColumn"));
            dbTexts.add(validation.get("description"));
        }
        for (Map<String, String> validation : dbColumnValidations) {
            dbTexts.add(validation.get("expectedValueOrVariable"));
            dbTexts.add(validation.get("dbColumnName"));
        }
        return buildVariableDependenciesJson(dbColumnValidations, dbTexts.toArray(new String[0]));
    }

    private String runtimeVariableType(String name) {
        return switch (name) {
            case "randomInt" -> "integer";
            case "randomDate" -> "date";
            default -> "string";
        };
    }

    private void collectVariableNames(String text, Set<String> names) {
        if (text == null || text.isBlank()) {
            return;
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\\$\\{([A-Za-z0-9_.-]+)}")
                .matcher(text);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
    }

    private Map<String, String> parseQueryParams(String endpoint) {
        Map<String, String> params = new LinkedHashMap<>();
        int questionMark = endpoint.indexOf('?');
        if (questionMark < 0 || questionMark == endpoint.length() - 1) {
            return params;
        }
        String query = endpoint.substring(questionMark + 1);
        for (String pair : query.split("&")) {
            if (pair.isBlank()) {
                continue;
            }
            String[] parts = pair.split("=", 2);
            params.put(parts[0], parts.length > 1 ? parts[1] : "");
        }
        return params;
    }

    private Path currentTestSuiteWorkbookPath() {
        if (testSuiteWorkbookPathField == null) {
            return null;
        }
        String path = testSuiteWorkbookPathField.getText().trim();
        if (path.isBlank() || path.startsWith("Failed:")) {
            return null;
        }
        Path workbookPath = Path.of(path);
        return Files.exists(workbookPath) ? workbookPath : null;
    }

    private String currentText(JTextField field) {
        return field == null ? "" : field.getText().trim();
    }

    private void selectTopLevelResponseFields() {
        if (responseFieldsTableModel == null) {
            return;
        }
        for (int row = 0; row < responseFieldsTableModel.getRowCount(); row++) {
            String path = stringCellValue(responseFieldsTableModel, row, 1);
            String withoutRoot = path.startsWith("$.") ? path.substring(2) : path;
            boolean topLevel = !withoutRoot.contains(".") && !withoutRoot.contains("[");
            responseFieldsTableModel.setValueAt(topLevel, row, 0);
        }
    }

    private void saveSelectedResponseVariables() {
        if (responseFieldsTable != null && responseFieldsTable.isEditing()) {
            responseFieldsTable.getCellEditor().stopCellEditing();
        }
        int savedCount = 0;
        for (int row = 0; row < responseFieldsTableModel.getRowCount(); row++) {
            Object checked = responseFieldsTableModel.getValueAt(row, 0);
            if (!(checked instanceof Boolean) || !((Boolean) checked)) {
                continue;
            }
            String variableName = normalizeVariableName(stringCellValue(responseFieldsTableModel, row, 3));
            if (variableName.isBlank()) {
                variableName = defaultVariableNameFromPath(stringCellValue(responseFieldsTableModel, row, 1));
            }
            savedVariables.put(variableName, stringCellValue(responseFieldsTableModel, row, 5));
            savedVariablePaths.put(variableName, stringCellValue(responseFieldsTableModel, row, 1));
            savedVariableTypes.put(variableName, stringCellValue(responseFieldsTableModel, row, 4));
            savedCount++;
        }
        refreshSavedVariablesView();
        JOptionPane.showMessageDialog(this, savedCount + " variable(s) saved.");
    }

    private void saveVariablesToFile() {
        if (savedVariables.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No saved variables are available to save.",
                    "Save Variables", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("api-validator-variables.json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            JSONObject exportJson = new JSONObject();
            exportJson.put("format", "api-validator-response-variables");
            exportJson.put("version", 1);

            JSONArray variablesJson = new JSONArray();
            savedVariables.keySet().stream().sorted().forEach(name -> {
                JSONObject variableJson = new JSONObject();
                variableJson.put("name", name);
                variableJson.put("value", savedVariables.getOrDefault(name, ""));
                variableJson.put("jsonPath", savedVariablePaths.getOrDefault(name, ""));
                variableJson.put("type", savedVariableTypes.getOrDefault(name, "string"));
                variablesJson.put(variableJson);
            });
            exportJson.put("variables", variablesJson);

            String json = apiService.prettyPrintJson(exportJson.toString());
            Files.writeString(chooser.getSelectedFile().toPath(), json);
            JOptionPane.showMessageDialog(this, variablesJson.length() + " variable(s) saved successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Variables Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importVariablesFromFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            JSONObject importJson = new JSONObject(Files.readString(chooser.getSelectedFile().toPath()));
            JSONArray variablesJson = importJson.optJSONArray("variables");
            if (variablesJson == null) {
                throw new IllegalArgumentException("Selected file does not contain a variables array.");
            }

            int importedCount = 0;
            for (int i = 0; i < variablesJson.length(); i++) {
                JSONObject variableJson = variablesJson.getJSONObject(i);
                String name = normalizeVariableName(variableJson.optString("name"));
                if (name.isBlank()) {
                    continue;
                }
                savedVariables.put(name, variableJson.optString("value"));
                savedVariablePaths.put(name, variableJson.optString("jsonPath"));
                savedVariableTypes.put(name, variableJson.optString("type", "string"));
                importedCount++;
            }

            refreshSavedVariablesView();
            JOptionPane.showMessageDialog(this, importedCount + " variable(s) imported.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Import Variables Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createSavedVariable() {
        JTextField nameField = new JTextField();
        JTextField valueField = new JTextField();
        JTextField jsonPathField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{
                "string", "number", "boolean", "json", "date", "datetime"
        });
        typeBox.setSelectedItem("string");

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 8));
        panel.add(createLabeledPanel("Variable", nameField));
        panel.add(createLabeledPanel("Value", valueField));
        panel.add(createLabeledPanel("Type", typeBox));
        panel.add(createLabeledPanel("JSON Path (Optional)", jsonPathField));

        while (true) {
            int option = JOptionPane.showConfirmDialog(this, panel,
                    "Create Variable", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option != JOptionPane.OK_OPTION) {
                return;
            }

            String variableName = normalizeVariableName(nameField.getText());
            if (variableName.isBlank()) {
                JOptionPane.showMessageDialog(this, "Enter a variable name.",
                        "Create Variable", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            savedVariables.put(variableName, valueField.getText());
            savedVariablePaths.put(variableName, jsonPathField.getText().trim());
            savedVariableTypes.put(variableName, String.valueOf(typeBox.getSelectedItem()));
            refreshSavedVariablesView();
            selectSavedVariable(variableName);
            JOptionPane.showMessageDialog(this, "Created ${" + variableName + "}.");
            return;
        }
    }

    private void refreshSavedVariablesView() {
        if (savedVariablesTableModel != null) {
            savedVariablesTableModel.setRowCount(0);
            Map<String, String> ordered = new LinkedHashMap<>();
            savedVariables.keySet().stream().sorted().forEach(key -> ordered.put(key, savedVariables.get(key)));
            for (Map.Entry<String, String> entry : ordered.entrySet()) {
                savedVariablesTableModel.addRow(new Object[]{
                        entry.getKey(),
                        entry.getValue(),
                        savedVariablePaths.getOrDefault(entry.getKey(), ""),
                        savedVariableTypes.getOrDefault(entry.getKey(), "string")
                });
            }
        }
        refreshVariableDropdowns();
    }

    private void selectSavedVariable(String variableName) {
        if (savedVariablesTable == null || savedVariablesTableModel == null) {
            return;
        }
        for (int row = 0; row < savedVariablesTableModel.getRowCount(); row++) {
            if (variableName.equals(stringCellValue(savedVariablesTableModel, row, 0))) {
                savedVariablesTable.setRowSelectionInterval(row, row);
                savedVariablesTable.scrollRectToVisible(savedVariablesTable.getCellRect(row, 0, true));
                return;
            }
        }
    }

    private void refreshVariableDropdowns() {
        for (JComboBox<String> dropdown : variableDropdowns) {
            if (dropdown == null) {
                continue;
            }
            Object selected = dropdown.getSelectedItem();
            dropdown.removeAllItems();
            savedVariables.keySet().stream().sorted().forEach(name -> dropdown.addItem("${" + name + "}"));
            if (dropdown == apiVariableDropdown) {
                addRuntimeVariableOptions(dropdown);
            }
            if (selected != null) {
                dropdown.setSelectedItem(selected);
            }
        }
        applyFieldValidationExpectedValueEditor();
        applyDbColumnValidationExpectedValueEditor();
    }

    private void removeSelectedSavedVariable() {
        if (savedVariablesTable == null) {
            return;
        }
        int row = savedVariablesTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        String name = stringCellValue(savedVariablesTableModel, row, 0);
        savedVariables.remove(name);
        savedVariablePaths.remove(name);
        savedVariableTypes.remove(name);
        refreshSavedVariablesView();
    }

    private JComboBox<String> createVariableDropdown() {
        JComboBox<String> dropdown = new JComboBox<>();
        dropdown.setEditable(true);
        dropdown.setFont(UI_FONT);
        dropdown.setPreferredSize(new Dimension(210, 38));
        variableDropdowns.add(dropdown);
        savedVariables.keySet().stream().sorted().forEach(name -> dropdown.addItem("${" + name + "}"));
        return dropdown;
    }

    private void addRuntimeVariableOptions(JComboBox<String> dropdown) {
        for (String variable : RUNTIME_VARIABLES) {
            dropdown.addItem(variable);
        }
    }

    private void applyFieldValidationExpectedValueEditor() {
        if (fieldValidationsTable == null || fieldValidationsTable.getColumnModel().getColumnCount() <= 5) {
            return;
        }
        JComboBox<String> expectedDropdown = new JComboBox<>();
        expectedDropdown.setEditable(true);
        expectedDropdown.setFont(UI_FONT);
        savedVariables.keySet().stream().sorted().forEach(name -> expectedDropdown.addItem("${" + name + "}"));
        fieldValidationsTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(expectedDropdown));
    }

    private void applyDbColumnValidationExpectedValueEditor() {
        if (dbColumnValidationsTable == null || dbColumnValidationsTable.getColumnModel().getColumnCount() <= 5) {
            return;
        }
        JComboBox<String> expectedDropdown = new JComboBox<>();
        expectedDropdown.setEditable(true);
        expectedDropdown.setFont(UI_FONT);
        savedVariables.keySet().stream().sorted().forEach(name -> expectedDropdown.addItem("${" + name + "}"));
        dbColumnValidationsTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(expectedDropdown));
    }

    private void insertSelectedVariable(JTextComponent target, JComboBox<String> dropdown) {
        if (target == null || dropdown == null || dropdown.getSelectedItem() == null) {
            return;
        }
        String selected = String.valueOf(dropdown.getSelectedItem());
        if (dropdown == apiVariableDropdown && RUNTIME_VARIABLES.contains(selected)) {
            selected = "${" + selected + "}";
        }
        target.replaceSelection(selected);
    }

    private void insertSelectedVariableIntoWebStep() {
        if (webStepsTable == null || webVariableDropdown == null || webVariableDropdown.getSelectedItem() == null) {
            return;
        }
        int row = webStepsTable.getSelectedRow();
        int column = webStepsTable.getSelectedColumn();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a web step row before inserting a variable.",
                    "Web Testing", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (column != 2 && column != 3) {
            column = 3;
        }
        String existing = stringCellValue(webStepsTableModel, row, column);
        webStepsTableModel.setValueAt(existing + String.valueOf(webVariableDropdown.getSelectedItem()), row, column);
    }

    private String resolveVariablesInText(String text) {
        if (text == null || text.isBlank() || savedVariables.isEmpty()) {
            return text == null ? "" : text;
        }
        String resolved = text;
        for (Map.Entry<String, String> entry : savedVariables.entrySet()) {
            resolved = resolved.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return resolved;
    }

    private String resolveRuntimeVariablesInText(String text) {
        if (text == null || text.isBlank()) {
            return text == null ? "" : text;
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\\$\\{(randomString|randomInt|randomDate)}")
                .matcher(text);
        StringBuffer resolved = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(resolved,
                    java.util.regex.Matcher.quoteReplacement(generateRuntimeVariableValue(matcher.group(1))));
        }
        matcher.appendTail(resolved);
        return resolved.toString();
    }

    private String generateRuntimeVariableValue(String variableName) {
        return switch (variableName) {
            case "randomString" -> randomAlphaNumeric(12);
            case "randomInt" -> String.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            case "randomDate" -> LocalDate.now()
                    .plusDays(ThreadLocalRandom.current().nextInt(0, 366))
                    .toString();
            default -> "";
        };
    }

    private String randomAlphaNumeric(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder value = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            value.append(alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length())));
        }
        return value.toString();
    }

    private Map<String, String> snapshotSavedVariables() {
        return new HashMap<>(savedVariables);
    }

    private String normalizeVariableName(String name) {
        String normalized = name == null ? "" : name.trim();
        if (normalized.startsWith("${") && normalized.endsWith("}")) {
            normalized = normalized.substring(2, normalized.length() - 1).trim();
        }
        return normalized;
    }

    private String defaultVariableName(ResponseFieldCandidate field) {
        if (field.jsonPath != null && field.jsonPath.contains("[")) {
            return defaultVariableNameFromPath(field.jsonPath);
        }
        if (field.fieldName != null && !field.fieldName.isBlank()) {
            return sanitizeVariableName(field.fieldName);
        }
        return defaultVariableNameFromPath(field.jsonPath);
    }

    private String defaultVariableNameFromPath(String path) {
        String normalized = path == null ? "value" : path.replace("$.", "").replace("$", "root");
        return sanitizeVariableName(normalized);
    }

    private String sanitizeVariableName(String raw) {
        String sanitized = raw == null ? "value" : raw.replaceAll("[^A-Za-z0-9_.-]", "_");
        return sanitized.isBlank() ? "value" : sanitized;
    }

    private ApiRequest buildCurrentRequest() {
        ApiRequest req = new ApiRequest();
        req.url = resolveVariablesInText(endpointField.getText().trim());
        req.method = methodDropdown.getSelectedItem().toString();
        req.body = isBodyAllowedForSelectedMethod() ? resolveVariablesInText(performanceBodyArea.getText().trim()) : "";
        req.headers = parseHeaders(resolveVariablesInText(headersArea.getText()));
        req.token = authTypeDropdown.getSelectedItem().equals("Bearer Token")
                ? new String(tokenField.getPassword()).trim()
                : "";
        req.token = resolveVariablesInText(req.token);
        return req;
    }

    private void appendPerformanceLog(String message) {
        performanceLogArea.append(message + System.lineSeparator());
        performanceLogArea.setCaretPosition(performanceLogArea.getDocument().getLength());
    }

    private void toggleTokenVisibility(JButton button) {
        if (tokenField.getEchoChar() == 0) {
            tokenField.setEchoChar(defaultEchoChar);
            button.setText("Show");
        } else {
            tokenField.setEchoChar((char) 0);
            button.setText("Hide");
        }
    }

    private void updateAuthControls(JButton toggleTokenBtn) {
        boolean bearerSelected = "Bearer Token".equals(authTypeDropdown.getSelectedItem());
        tokenField.setEnabled(bearerSelected);
        toggleTokenBtn.setEnabled(bearerSelected);
        if (!bearerSelected) {
            tokenField.setText("");
            tokenField.setEchoChar(defaultEchoChar);
            toggleTokenBtn.setText("Show");
        }
    }

    private void setRequestInProgress(boolean inProgress) {
        sendRequestButton.setEnabled(!inProgress);
        clearButton.setEnabled(!inProgress);
        endpointField.setEnabled(!inProgress);
        methodDropdown.setEnabled(!inProgress);
        authTypeDropdown.setEnabled(!inProgress);
        headersArea.setEnabled(!inProgress);
        bodyArea.setEnabled(!inProgress);
        if ("Bearer Token".equals(authTypeDropdown.getSelectedItem())) {
            tokenField.setEnabled(!inProgress);
        }
        if (isBodyAllowedForSelectedMethod()) {
            bodyArea.setEnabled(!inProgress);
            requestFormatDropdown.setEnabled(!inProgress);
        } else {
            bodyArea.setEnabled(false);
            requestFormatDropdown.setEnabled(false);
        }
    }

    private Map<String, String> parseHeaders(String text) {
        Map<String, String> map = new HashMap<>();
        for (String line : text.split("\n")) {
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                map.put(parts[0].trim(), parts[1].trim());
            }
        }
        return map;
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(PRIMARY);
        return label;
    }

    private void hideTableColumn(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }

    private JTextArea createEditorArea(String initialValue) {
        JTextArea area = new JTextArea(initialValue);
        area.setFont(MONO_FONT);
        area.setLineWrap(false);
        area.setTabSize(2);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        return area;
    }

    private JTextArea createResponseArea() {
        JTextArea area = createEditorArea("");
        area.setEditable(false);
        area.setBackground(PANEL_BG);
        return area;
    }

    private JScrollPane createLineNumberScrollPane(JTextArea area) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.setRowHeaderView(createLineNumberView(area));
        return scrollPane;
    }

    private JScrollPane createPrettyPane() {
        prettyResponseArea = createResponseArea();
        JScrollPane scrollPane = new JScrollPane(prettyResponseArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setRowHeaderView(createLineNumberView(prettyResponseArea));
        return scrollPane;
    }

    private JComponent createLineNumberView(JTextArea textArea) {
        JTextArea lines = new JTextArea("1");
        lines.setBackground(new Color(248, 250, 253));
        lines.setEditable(false);
        lines.setFont(MONO_FONT);
        lines.setBorder(new EmptyBorder(10, 8, 10, 8));
        lines.setForeground(new Color(95, 103, 120));

        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateLineNumbers();
            }

            private void updateLineNumbers() {
                int linesCount = Math.max(1, textArea.getLineCount());
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i <= linesCount; i++) {
                    builder.append(i).append(System.lineSeparator());
                }
                lines.setText(builder.toString());
            }
        });
        return lines;
    }

    private JPanel createEditorCard(String title, JComponent editor, JComponent footer) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(PANEL_BG);

        JLabel label = createSectionTitle(title);
        card.add(label, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setBackground(PANEL_BG);
        body.add(editor, BorderLayout.CENTER);
        if (footer != null) {
            body.add(footer, BorderLayout.SOUTH);
        }

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createEditorCardWithTrackedTitle(String title, JComponent editor, JComponent footer) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(PANEL_BG);

        requestBodyTitleLabel = createSectionTitle(title);
        card.add(requestBodyTitleLabel, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setBackground(PANEL_BG);
        body.add(editor, BorderLayout.CENTER);
        if (footer != null) {
            body.add(footer, BorderLayout.SOUTH);
        }

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTrackedPerformanceBodyCard(String title, JComponent editor, JComponent footer) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(PANEL_BG);

        performanceBodyTitleLabel = createSectionTitle(title);
        card.add(performanceBodyTitleLabel, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setBackground(PANEL_BG);
        body.add(editor, BorderLayout.CENTER);
        if (footer != null) {
            body.add(footer, BorderLayout.SOUTH);
        }

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createResponseTabPanel(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel wrapCard(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        panel.add(createSectionTitle(title), BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMetricCard(String title, JLabel primaryValue, JLabel secondaryValue) {
        JPanel card = new JPanel();
        card.setBackground(new Color(249, 251, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(60, 66, 79));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        primaryValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        secondaryValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(primaryValue);
        card.add(Box.createVerticalStrut(4));
        card.add(secondaryValue);
        return card;
    }

    private JPanel createMetricPanel(String labelText, JLabel valueLabel) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(PANEL_BG);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(60, 66, 79));
        panel.add(label);
        panel.add(valueLabel);
        return panel;
    }

    private JLabel createMetricValue(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(color);
        return label;
    }

    private JSpinner createSpinner(int value, int min, int max, int step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        spinner.setFont(UI_FONT);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setColumns(6);
            ((JSpinner.DefaultEditor) editor).getTextField().setFont(UI_FONT);
        }
        return spinner;
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(PANEL_BG);
        JLabel label = new JLabel(labelText);
        label.setFont(UI_FONT);
        component.setFont(UI_FONT);
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCompactLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(PANEL_BG);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(35, 44, 58));
        component.setFont(UI_FONT);
        component.setPreferredSize(new Dimension(component.getPreferredSize().width, 42));
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void addWebField(JPanel panel, int gridX, int gridY, String labelText, JComponent field,
                             double weightX, Insets insets) {
        addWebField(panel, gridX, gridY, labelText, field, weightX, insets, 1);
    }

    private void addWebField(JPanel panel, int gridX, int gridY, String labelText, JComponent field,
                             double weightX, Insets insets, int gridWidth) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.gridwidth = gridWidth;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = weightX;
        gbc.insets = insets;
        panel.add(createCompactLabeledPanel(labelText, field), gbc);
    }

    private void addPerformanceConfigField(JPanel panel, int gridX, String labelText, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = gridX;
        gbc.insets = new Insets(0, 0, 0, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(35, 44, 58));
        panel.add(label, gbc);

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = gridX + 1;
        gbc.insets = new Insets(0, 0, 0, 28);
        gbc.anchor = GridBagConstraints.WEST;
        field.setPreferredSize(new Dimension(140, 38));
        panel.add(field, gbc);
    }

    private void addLabeledField(JPanel panel, int gridX, String labelText, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = gridX;
        gbc.insets = new Insets(0, 0, 0, 12);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(labelText), gbc);

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = gridX + 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = gridX == 2 ? 1.0 : 0.18;
        field.setPreferredSize(new Dimension(gridX == 2 ? 600 : 150, 42));
        panel.add(field, gbc);
    }

    private JComponent wrapWithTrailingButton(JComponent field, JComponent button) {
        JPanel panel = new JPanel(new BorderLayout(6, 0));
        panel.setBackground(PANEL_BG);
        panel.add(field, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }

    private JComponent wrapComponent(JComponent component) {
        component.setFont(UI_FONT);
        return component;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 28, 12, 28));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UI_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(35, 44, 58));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 18, 10, 18)
        );
        button.setBorder(border);
        return button;
    }

    private void highlightJson(JTextArea target, String json) {
        target.setText(json);
    }

    private List<Object[]> filterMatchedRows(List<Object[]> results) {
        List<Object[]> matched = new ArrayList<>();
        for (Object[] row : results) {
            if (row.length > 0 && "Match".equals(String.valueOf(row[0]))) {
                matched.add(row);
            }
        }
        return matched;
    }

    private static String resolveAppVersion() {
        Package appPackage = ApiValidatorUI.class.getPackage();
        if (appPackage != null && appPackage.getImplementationVersion() != null) {
            return "v" + appPackage.getImplementationVersion();
        }
        return "dev";
    }

    private String formatDuration(java.time.Duration duration) {
        if (duration == null) {
            return "--";
        }
        long millis = duration.toMillis();
        if (millis < 1000) {
            return millis + " ms";
        }
        return String.format("%.2f s", millis / 1000.0);
    }

    private void updateRequestBodyState() {
        boolean bodyAllowed = isBodyAllowedForSelectedMethod();
        bodyArea.setEnabled(bodyAllowed);
        requestFormatDropdown.setEnabled(bodyAllowed);
        requestBodyTitleLabel.setText(bodyAllowed ? "Request Body (Optional)" : "Request Body (Not Used For GET/DELETE)");
        if (!bodyAllowed) {
            bodyArea.setText("");
        }
    }

    private boolean isBodyAllowedForSelectedMethod() {
        Object selectedMethod = methodDropdown.getSelectedItem();
        return selectedMethod != null
                && !"GET".equals(selectedMethod.toString())
                && !"DELETE".equals(selectedMethod.toString());
    }

    private void updatePerformanceBodyState() {
        if (performanceBodyArea == null || performanceBodyTitleLabel == null) {
            return;
        }
        boolean bodyAllowed = isBodyAllowedForSelectedMethod();
        performanceBodyArea.setEnabled(bodyAllowed);
        performanceBodyTitleLabel.setText(bodyAllowed
                ? "Performance Request Body (POST/PUT/PATCH)"
                : "Performance Request Body (Not Used For GET/DELETE)");
        if (!bodyAllowed) {
            performanceBodyArea.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApiValidatorUI::new);
    }
}
