package app.birdsoft.meurestaurante.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EstabelecimentoHorario  implements Serializable {
    private String diaSemane;
    private boolean aberto;
    private String horarioAbrir;
    private String horarioFechar;

    public EstabelecimentoHorario(){}

    public EstabelecimentoHorario(String diaSemane, boolean aberto, String horarioAbrir, String horarioFechar) {
        this.diaSemane = diaSemane;
        this.aberto = aberto;
        this.horarioAbrir = horarioAbrir;
        this.horarioFechar = horarioFechar;
    }

    public String getDiaSemane() {
        return diaSemane;
    }

    public void setDiaSemane(String diaSemane) {
        this.diaSemane = diaSemane;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public String getHorarioAbrir() {
        return horarioAbrir;
    }

    public void setHorarioAbrir(String horarioAbrir) {
        this.horarioAbrir = horarioAbrir;
    }

    public String getHorarioFechar() {
        return horarioFechar;
    }

    public void setHorarioFechar(String horarioFechar) {
        this.horarioFechar = horarioFechar;
    }

    public List<EstabelecimentoHorario> getList() {
        List<EstabelecimentoHorario> horarioList = new ArrayList<>();
        EstabelecimentoHorario dom = new EstabelecimentoHorario();
        dom.setAberto(false);
        dom.setDiaSemane("dom");
        dom.setHorarioAbrir("--:--");
        dom.setHorarioFechar("--:--");
        horarioList.add(dom);
        EstabelecimentoHorario seg = new EstabelecimentoHorario();
        seg.setAberto(false);
        seg.setDiaSemane("seg");
        seg.setHorarioAbrir("--:--");
        seg.setHorarioFechar("--:--");
        horarioList.add(seg);
        EstabelecimentoHorario ter = new EstabelecimentoHorario();
        ter.setAberto(false);
        ter.setDiaSemane("ter");
        ter.setHorarioAbrir("--:--");
        ter.setHorarioFechar("--:--");
        horarioList.add(ter);
        EstabelecimentoHorario qua = new EstabelecimentoHorario();
        qua.setAberto(false);
        qua.setDiaSemane("qua");
        qua.setHorarioAbrir("--:--");
        qua.setHorarioFechar("--:--");
        horarioList.add(qua);
        EstabelecimentoHorario qui = new EstabelecimentoHorario();
        qui.setAberto(false);
        qui.setDiaSemane("qui");
        qui.setHorarioAbrir("--:--");
        qui.setHorarioFechar("--:--");
        horarioList.add(qui);
        EstabelecimentoHorario sex = new EstabelecimentoHorario();
        sex.setAberto(false);
        sex.setDiaSemane("sex");
        sex.setHorarioAbrir("--:--");
        sex.setHorarioFechar("--:--");
        horarioList.add(sex);
        EstabelecimentoHorario sab = new EstabelecimentoHorario();
        sab.setAberto(false);
        sab.setDiaSemane("sab");
        sab.setHorarioAbrir("--:--");
        sab.setHorarioFechar("--:--");
        horarioList.add(sab);

        return horarioList;
    }
}
