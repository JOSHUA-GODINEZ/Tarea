
package cr.ac.una.tarea.model;
 
public class KioscoData {
    public String id;
    public String tramite;
    public String ficha;
    public Boolean Preferencial;
    public String fecha;
    public String Sucursal;
    public String audio;

public void setAudio(String audio) {
    this.audio = audio;
}

public String getAudio() {
    return audio;
}


    public void setTxtId(String txtId) { this.id = txtId; }
    public void setPreference(boolean preference) { this.Preferencial = preference; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public String getTicketNumber() {
        return ficha;
    }
    
    public void setTicketNumber(String ticketNumber) {
      this.ficha=ticketNumber;
    }
    
    public String getId() {
        return id;
    }
    
public void setTramite(String tramite) {
    this.tramite = tramite;
}
    
    public boolean getPreference() {
        return Preferencial;
    }
    
public void cargar(KioscoData otro) {
    this.id = otro.id;
    this.tramite = otro.tramite;
    this.ficha = otro.ficha;
    this.Preferencial = otro.Preferencial;
    this.fecha = otro.fecha;
    this.Sucursal = otro.Sucursal;
    this.audio = otro.audio;
}
    
}
