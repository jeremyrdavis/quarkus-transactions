package org.acme.domain.commands;

public class SomeOtherKindOfCommand {

    String somethingToDo;

    public SomeOtherKindOfCommand(String somethingToDo) {
        this.somethingToDo = somethingToDo;
    }

    public SomeOtherKindOfCommand() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SomeOtherKindOfCommand{");
        sb.append("somethingToDo='").append(somethingToDo).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SomeOtherKindOfCommand that = (SomeOtherKindOfCommand) o;

        return somethingToDo != null ? somethingToDo.equals(that.somethingToDo) : that.somethingToDo == null;
    }

    @Override
    public int hashCode() {
        return somethingToDo != null ? somethingToDo.hashCode() : 0;
    }

    public String getSomethingToDo() {
        return somethingToDo;
    }
}
