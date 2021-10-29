package org.acme.domain.commands;

public class SomeKindOfCommand {

    String whatToDo;

    public SomeKindOfCommand(String whatToDo) {
        this.whatToDo = whatToDo;
    }

    public SomeKindOfCommand() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SomeKindOfCommand{");
        sb.append("whatToDo='").append(whatToDo).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SomeKindOfCommand that = (SomeKindOfCommand) o;

        return whatToDo != null ? whatToDo.equals(that.whatToDo) : that.whatToDo == null;
    }

    @Override
    public int hashCode() {
        return whatToDo != null ? whatToDo.hashCode() : 0;
    }

    public String getWhatToDo() {
        return whatToDo;
    }
}
