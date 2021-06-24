package app.birdsoft.meurestaurante.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemCardapioLista implements Serializable {
    public String displayName = "";
    public ArrayList<String> contents = new ArrayList<>();
    public ArrayList<Double> valores = new ArrayList<>();
    public ArrayList<Integer> quantidate = new ArrayList<>();

}
