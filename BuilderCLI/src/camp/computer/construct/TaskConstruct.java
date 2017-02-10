package camp.computer.construct;

public class TaskConstruct extends Construct {

    // (task (rev0, rev1, rev2, ..., revN))

    public ScriptConstruct scriptConstruct = null;

    // TODO: execute-condition

    // TODO: input-data

    // TODO: repeat-strategy [ once, number(1,infinite), condition ]

    // TODO: type [ script-uuid, code-uuid ]

    // TODO: jump-policy

    public TaskConstruct() {
        super();
        this.type = "task";
    }

}
