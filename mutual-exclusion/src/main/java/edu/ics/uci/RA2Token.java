package edu.ics.uci;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class RA2Token {
    //public Map<Integer, Timestamp> lastAccess;
    private int id;

    public RA2Token(){
        id = 0;
        //lastAccess = new TreeMap<>();
    }

    public RA2Token(int id){
        this.id = id;
    }

    public RA2Token(RA2Token another){
        this.id = another.id;
    }

    public void setId(int id){this.id = id; }

    public Integer getId(){ return this.id; }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if (o==null || getClass() != o.getClass()) return false;
        RA2Token that =(RA2Token) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {return Objects.hash(id);}

    @Override
    public String toString() {
        return "RA2Token{" +
                "id=" +id +"}";
    }

}
