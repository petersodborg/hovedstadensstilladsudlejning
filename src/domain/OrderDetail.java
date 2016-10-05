package domain;
public class OrderDetail
{
    int id;
    int ordreFK;
    int resFK;
    int antal;
    String delnavn;

    public OrderDetail()
    {
        
    }
    public OrderDetail(int id, int ordreFK, int resFK,int antal) {
        this.id = id;
        this.ordreFK = ordreFK;
        this.resFK = resFK;
        this.antal = antal;
    }
    public OrderDetail(int id, int ordreFK, int resFK,int antal, String delnavn)
    {
         this.id = id;
        this.ordreFK = ordreFK;
        this.resFK = resFK;
        this.antal = antal;
        this.delnavn = delnavn;
    }

    public int getAntal() {
        return antal;
    }

    public int getId() {
        return id;
    }

    public int getOrdreFK() {
        return ordreFK;
    }

    public int getResFK() {
        return resFK;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "id=" + id + ", ordreFK=" + ordreFK + ", resFK=" + resFK + ", antal=" + antal + ", delnavn=" + delnavn + '}';
    }

    public String getDelnavn() {
        return delnavn;
    }




}

