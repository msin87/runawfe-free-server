package ru.runa.wfe.var.format;

import lombok.Getter;
import org.junit.Test;
import ru.runa.wfe.var.WfVariableType;

import static org.junit.Assert.*;

@Getter
class VariableContainer {
    private ActorFormat actorFormat;
    private BigDecimalFormat bigDecimalFormat;
    private BooleanFormat booleanFormat;
    private DateFormat dateFormat;
    private DateTimeFormat dateTimeFormat;
    private DoubleFormat doubleFormat;
    private ExecutorFormat executorFormat;
    private FileFormat fileFormat;
    private FormattedTextFormat formattedTextFormat;
    private GroupFormat groupFormat;
    private ListFormat listFormat;
    private LongFormat longFormat;
    private MapFormat mapFormat;
    private ProcessIdFormat processIdFormat;
    private StringFormat stringFormat;
    private TextFormat textFormat;
    private TimeFormat timeFormat;
    private UserTypeFormat userTypeFormat;
    private WfVariableType variableType;

    public VariableContainer(ActorFormat actorFormat) {
        this.actorFormat = actorFormat;
        variableType = WfVariableType.ACTOR;
    }

    public VariableContainer(BigDecimalFormat bigDecimalFormat) {
        this.bigDecimalFormat = bigDecimalFormat;
        variableType = WfVariableType.BIG_DECIMAL;
    }

    public VariableContainer(BooleanFormat booleanFormat) {
        this.booleanFormat = booleanFormat;
        variableType = WfVariableType.BOOLEAN;
    }

    public VariableContainer(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        variableType = WfVariableType.DATE;
    }

    public VariableContainer(DateTimeFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
        variableType = WfVariableType.DATE_TIME;
    }

    public VariableContainer(DoubleFormat doubleFormat) {
        this.doubleFormat = doubleFormat;
        variableType = WfVariableType.DOUBLE;
    }

    public VariableContainer(ExecutorFormat executorFormat) {
        this.executorFormat = executorFormat;
        variableType = WfVariableType.EXECUTOR;
    }

    public VariableContainer(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        variableType = WfVariableType.FILE;
    }

    public VariableContainer(FormattedTextFormat formattedTextFormat) {
        this.formattedTextFormat = formattedTextFormat;
        variableType = WfVariableType.FORMATTED_TEXT;
    }

    public VariableContainer(GroupFormat groupFormat) {
        this.groupFormat = groupFormat;
        variableType = WfVariableType.GROUP;
    }

    public VariableContainer(ListFormat listFormat) {
        this.listFormat = listFormat;
        variableType = WfVariableType.LIST;
    }

    public VariableContainer(LongFormat longFormat) {
        this.longFormat = longFormat;
        variableType = WfVariableType.LONG;
    }

    public VariableContainer(MapFormat mapFormat) {
        this.mapFormat = mapFormat;
        variableType = WfVariableType.MAP;
    }

    public VariableContainer(ProcessIdFormat processIdFormat) {
        this.processIdFormat = processIdFormat;
        variableType = WfVariableType.PROCESS_ID;
    }

    public VariableContainer(StringFormat stringFormat) {
        this.stringFormat = stringFormat;
        variableType = WfVariableType.STRING;
    }

    public VariableContainer(TextFormat textFormat) {
        this.textFormat = textFormat;
        variableType = WfVariableType.TEXT;
    }

    public VariableContainer(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
        variableType = WfVariableType.TIME;
    }

    public VariableContainer(UserTypeFormat userTypeFormat) {
        this.userTypeFormat = userTypeFormat;
        variableType = WfVariableType.USER_TYPE;
    }

    /*
        public VariableContainer(XMLNode xmlNode) {
           switch-case: set this.variableType and set one of the
            fields according to the XML parse result
        }

        public VariableContainer(SomeObject someObject){
            switch-case: set this.variableType and set one of the
            fields according to the someObject parse result
        }
     */
}

public class VariableTypedTest {
    @Test
    public void enumTest() {
        WfVariableType wfVariableType = WfVariableType.USER_TYPE;
        assertEquals(wfVariableType.toString(), "Пользовательский тип данных");
        WfVariableType wfVariableType1 = WfVariableType.fromString("Пользовательский тип данных");
        assertEquals(wfVariableType, wfVariableType1);
        try {
            WfVariableType wfVariableType2 = WfVariableType.fromString("asd");
            assertTrue(true);
        } catch (IllegalStateException exception) {
            assertFalse(false);
        }
    }

    @Test
    public void variableContainerTest() {
        VariableContainer variableContainer = new VariableContainer(new TimeFormat());
        switch (variableContainer.getVariableType()){
            case TIME:
                assertNotNull(variableContainer.getTimeFormat());
                break;
            case GROUP:
                assertNotNull(variableContainer.getGroupFormat());
                break;
            case DATE:
                assertNotNull(variableContainer.getDateFormat());
                break;
            case DATE_TIME:
                assertNotNull(variableContainer.getDateTimeFormat());
                break;
            case DOUBLE:
                assertNotNull(variableContainer.getDoubleFormat());
                break;
            case EXECUTOR:
                assertNotNull(variableContainer.getExecutorFormat());
                break;
            case MAP:
                assertNotNull(variableContainer.getMapFormat());
                break;
            case ACTOR:
                assertNotNull(variableContainer.getActorFormat());
                break;
            case USER_TYPE:
                assertNotNull(variableContainer.getUserTypeFormat());
                break;
            case LIST:
                assertNotNull(variableContainer.getListFormat());
                break;
            case PROCESS_ID:
                assertNotNull(variableContainer.getProcessIdFormat());
                break;
            case STRING:
                assertNotNull(variableContainer.getStringFormat());
                break;
            case TEXT:
                assertNotNull(variableContainer.getTextFormat());
                break;
            case FORMATTED_TEXT:
                assertNotNull(variableContainer.getFormattedTextFormat());
                break;
            case FILE:
                assertNotNull(variableContainer.getFileFormat());
                break;
            case BOOLEAN:
                assertNotNull(variableContainer.getBooleanFormat());
                break;
            case LONG:
                assertNotNull(variableContainer.getLongFormat());
                break;
            case BIG_DECIMAL:
                assertNotNull(variableContainer.getBigDecimalFormat());
                break;
        }
    }
}
