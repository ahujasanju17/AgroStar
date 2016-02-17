package haftaaa.voidmain.com.agrostar;

/**
 * Created by sanju on 03-01-2016.
 */
public class ListItem {

    public int icon;
    public String title;


    public String subtitle;

    public ListItem(){
        super();
    }

    public ListItem( String title ,int icon , String subtitle ){
        this.title=title;
        this.icon=icon;
        this.subtitle = subtitle;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }


}
