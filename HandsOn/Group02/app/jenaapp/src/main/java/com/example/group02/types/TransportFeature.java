package com.example.group02.types;

public class TransportFeature {

    private String type, name;

    @SuppressWarnings("unused")
    private TransportFeature(){}

    public TransportFeature(String type, String name){
        this.name =  Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
        setType(type);
    }

    private void setType(String type){
        String[] t = type.split("/");
        switch(t[t.length-2]){
            case "bikeStation": this.type = "Bike station"; break;
            case "chargeStation": this.type = "EV charging station"; break;
            default: this.type = "Metro station";
        }
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    
    
}
