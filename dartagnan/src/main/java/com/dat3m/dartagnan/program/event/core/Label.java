package com.dat3m.dartagnan.program.event.core;

import com.dat3m.dartagnan.program.event.Tag;
import com.dat3m.dartagnan.program.event.visitors.EventVisitor;

import java.util.HashSet;
import java.util.Set;

public class Label extends Event {

    private String name;
    private final Set<CondJump> jumpSet;

    public Label(String name){
        this.name = name;
        this.jumpSet = new HashSet<>();
        addFilters(Tag.ANY, Tag.LABEL);
    }

    protected Label(Label other){
        super(other);
        this.jumpSet = new HashSet<>();
        this.name = other.name;
    }

    public String getName(){ return name; }
    public void setName(String name) { this.name = name;}

    public Set<CondJump> getJumpSet() { return jumpSet; }

    @Override
    public void delete() {
        // We delete all jumps that target this label to avoid
        // broken jumps.
        getJumpSet().forEach(CondJump::delete);
        super.delete();
    }

    @Override
    public String toString(){
        return name + ":" + (is(Tag.SPINLOOP) ? "\t### SPINLOOP" : "");
    }

    // Unrolling
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Label getCopy(){
        return new Label(this);
    }

    // Visitor
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T accept(EventVisitor<T> visitor) {
		return visitor.visitLabel(this);
	}
}