package com.simpleweather.app.model;

public class RecyclerViewItem {

	public static final int H_TYPE = 0; //view类型0,header，横跨4列
    public static final int M_TYPE = 1; //view类型2，middler，横跨1列
    public static final int I_TYPE = 2;//view 类型3，item，横跨2列
	private int type;
	
    private int img;	//图标，header中代表现在天气图标，middler中代表现在天气图标,item中代表指数图标
    private String title;    //标题，header中代表现在温度，middler中代表日期，item中代表指数名称
    private String description;    //描述，header中代表现在天气情况，middler中代表现在天气情况，item中代表指数情况

    private String temp1;//header中代表风力及等级，middler中代表最低温度
    private String temp2;//header中代表发布时间，middler中代表最高温度
    
    
    public RecyclerViewItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RecyclerViewItem(int type, int img, String title,
			String description, String temp1, String temp2) {
		super();
		this.type = type;
		this.img = img;
		this.title = title;
		this.description = description;
		this.temp1 = temp1;
		this.temp2 = temp2;
	}

	//getter 和 setter

	public int getImg() {
        return img;
    }

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	@Override
    public String toString() {
        return "Item [img=" + img + ", title=" + title + ", description="
                + description + ", temp1=" + temp1 + ", temp2="
                        + temp2 + "]";
    }
}
