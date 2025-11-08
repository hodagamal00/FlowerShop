package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UpdateMessage implements Serializable {

    private String updateClass;
    private String updateFunction ;
    private String deleteId;
    private int id;
    private Product product = null;
    private Account account = null;
    private Worker worker = null;
    private Manager manager = null;
    private Order order = null;
    private Complaint complaint = null;
    private Message message;


    public UpdateMessage(String update_class, String update_function){
        this.updateClass = update_class;
        this.updateFunction = update_function;

    }
    public Complaint getComplaint()
    {   return complaint; }

    public void setComplaint(Complaint complaint)
    { this.complaint = complaint; }


    public String getUpdateClass(){
        return this.updateClass;
    }
    public String getUpdateFunction(){
        return this.updateFunction;
    }

    /**
     * Set the operation/function that should be performed on the updateClass.  This
     * complements the getUpdateFunction() accessor and allows controllers to
     * specify actions such as "add" or "edit" when constructing an
     * UpdateMessage.  Without this setter the code will not compile when
     * calling message.setUpdateFunction(...).
     *
     * @param updateFunction the name of the update function to perform
     */
    public void setUpdateFunction(String updateFunction) {
        this.updateFunction = updateFunction;
    }

    public String getDelteId(){
        return this.deleteId;
    }

    public int getId(){
        return this.id;
    }

    public Product getProduct(){
        return this.product;
    }

    public Account getAccount(){
        return this.account;
    }

    public void setProduct(Product prod){
        this.product = prod;
    }

    public void setAccount(Account acc){
        this.account = acc;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setDelteId(String delete_id){
        this.deleteId = delete_id;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    public Message getMessage()
    {   return message; }

    public void setMessage(Message message)
    { this.message=message; }
}

