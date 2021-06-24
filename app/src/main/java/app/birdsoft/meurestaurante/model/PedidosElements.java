package app.birdsoft.meurestaurante.model;

import android.view.View;

import app.birdsoft.meurestaurante.tools.Status;

public class PedidosElements {
    private boolean isPedido;
    private Status statusPedido;
    private Pedido pedido;
    private int imageLogo = 0, layoutWifiOffline = View.GONE;
    private boolean cancelado, atrasado;
    private String endereco = "";
    private String status = "", mensagemCancelamento = "", previsaoDeEntrega = "";
    private int progressPreparando, progressTransporte, progressFinal;
    private int btnVerMsgCancelamentoPedido = View.GONE, layoutConexao = View.GONE, relogioView = View.VISIBLE, layoutPrincipal = View.GONE, errorDados = View.GONE, lytProgress = View.VISIBLE;
    private int btnAlteracaoPedido = View.GONE, btnComfirmarCancelamento = View.GONE, btnFinalizarPedido = View.GONE, btnRecebiPedido = View.GONE, btnRelatarProblemas = View.VISIBLE;

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        this.layoutWifiOffline = layoutWifiOffline;
    }

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public int getLayoutWifiOffline() {
        return layoutWifiOffline;
    }

    public int getBtnVerMsgCancelamentoPedido() {
        return btnVerMsgCancelamentoPedido;
    }

    public int getRelogioView() {
        return relogioView;
    }

    public void setRelogioView(int relogioView) {
        this.relogioView = relogioView;
    }

    public void setBtnVerMsgCancelamentoPedido(int btnVerMsgCancelamentoPedido) {
        this.btnVerMsgCancelamentoPedido = btnVerMsgCancelamentoPedido;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    public boolean isAtrasado() {
        return atrasado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public Status getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(Status statusPedido) {
        this.statusPedido = statusPedido;
    }

    public int getProgressPreparando() {
        return progressPreparando;
    }

    public String getMensagemCancelamento() {
        return mensagemCancelamento;
    }

    public String getPrevisaoDeEntrega() {
        return previsaoDeEntrega;
    }

    public void setMensagemCancelamento(String mensagemCancelamento) {
        this.mensagemCancelamento = mensagemCancelamento;
    }

    public void setPrevisaoDeEntrega(String previsaoDeEntrega) {
        this.previsaoDeEntrega = previsaoDeEntrega;
    }

    public void setProgressPreparando(int progressPreparando) {
        this.progressPreparando = progressPreparando;
    }

    public int getProgressTransporte() {
        return progressTransporte;
    }

    public void setProgressTransporte(int progressTransporte) {
        this.progressTransporte = progressTransporte;
    }

    public int getProgressFinal() {
        return progressFinal;
    }

    public void setProgressFinal(int progressFinal) {
        this.progressFinal = progressFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(int imageLogo) {
        this.imageLogo = imageLogo;
    }

    public PedidosElements(boolean isPedido, Pedido pedido) {
        this.isPedido = isPedido;
        this.pedido = pedido;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getLayoutPrincipal() {
        return layoutPrincipal;
    }

    public void setLayoutPrincipal(int layoutPrincipal) {
        this.layoutPrincipal = layoutPrincipal;
    }

    public int getErrorDados() {
        return errorDados;
    }

    public void setErrorDados(int errorDados) {
        this.errorDados = errorDados;
    }

    public int getLytProgress() {
        return lytProgress;
    }

    public void setLytProgress(int lytProgress) {
        this.lytProgress = lytProgress;
    }

    public int getBtnAlteracaoPedido() {
        return btnAlteracaoPedido;
    }

    public void setBtnAlteracaoPedido(int btnAlteracaoPedido) {
        this.btnAlteracaoPedido = btnAlteracaoPedido;
    }

    public int getBtnComfirmarCancelamento() {
        return btnComfirmarCancelamento;
    }

    public void setBtnComfirmarCancelamento(int btnComfirmarCancelamento) {
        this.btnComfirmarCancelamento = btnComfirmarCancelamento;
    }

    public int getBtnFinalizarPedido() {
        return btnFinalizarPedido;
    }

    public void setBtnFinalizarPedido(int btnFinalizarPedido) {
        this.btnFinalizarPedido = btnFinalizarPedido;
    }

    public int getBtnRecebiPedido() {
        return btnRecebiPedido;
    }

    public void setBtnRecebiPedido(int btnRecebiPedido) {
        this.btnRecebiPedido = btnRecebiPedido;
    }

    public int getBtnRelatarProblemas() {
        return btnRelatarProblemas;
    }

    public void setBtnRelatarProblemas(int btnRelatarProblemas) {
        this.btnRelatarProblemas = btnRelatarProblemas;
    }

    public PedidosElements() {
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(boolean pedido) {
        isPedido = pedido;
    }

    public boolean isPedido() {
        return isPedido;
    }
}
