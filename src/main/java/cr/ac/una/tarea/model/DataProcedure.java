package cr.ac.una.tarea.model;

public class DataProcedure {
    private String name;
    private String price;
    private String detail;
    private Boolean state;
   

    public void setName(String name){
    this.name=name;
    }
   public void setprice(String price){
    this.price=price;
    }
     public  void setDetail(String detail){
    this.detail=detail;
    }
    public void setState(Boolean state){
    this.state=state;
    }

    public String getName(){
    return name;
    }
   public String getPrice(){
    return price;
    }
    public String getDetail(){
    return detail;
    }
   public Boolean getState(){
    return state;
    }
}
