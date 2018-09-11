package pohybytovaru.innovativeproposals.com.pohybytovaru;

import android.app.Application;

public class Globals  {
    private static Globals mInstance= null;

    private int myTovarId;
    private int myTransakciaId;
    private int myMiestoFromId; // vozik/miestnost + - + id, cize napr. V-1 alebo M-2
    private int myMiestoToId;   // to iste

    public int getMyTovarId() {
        return myTovarId;
    }
    public void setMyTovarId(int myTovarId) {
        this.myTovarId = myTovarId;
    }

    public int getMyTransakciaId() {
        return myTransakciaId;
    }

    public void setMyTransakciaId(int myTransakciaId) {
        this.myTransakciaId = myTransakciaId;
    }

    public int getMiestoToId() {
        return myMiestoToId;
    }

    public void setMyMiestoToId(int myMiestoToId) {
        this.myMiestoToId = myMiestoToId;
    }

    public int getMiestoFromId(){
        return myMiestoFromId;
    }
    public void setMiestoFromId(int  myMiestoFromId){
        this.myMiestoFromId = myMiestoFromId;
    }

    public static synchronized Globals getInstance() {
        if(null == mInstance){
            mInstance = new Globals();
        }
        return mInstance;
    }
}



