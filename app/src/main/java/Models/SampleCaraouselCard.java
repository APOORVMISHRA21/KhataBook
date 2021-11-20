package Models;

public class SampleCaraouselCard {

    int resourceId;
    String titleText, subTitleText;

    public SampleCaraouselCard(int resourceId, String titleText, String subTitleText){
        this.resourceId = resourceId;
        this.titleText = titleText;
        this.subTitleText = subTitleText;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getSubTitleText() {
        return subTitleText;
    }

    public void setSubTitleText(String subTitleText) {
        this.subTitleText = subTitleText;
    }
}
