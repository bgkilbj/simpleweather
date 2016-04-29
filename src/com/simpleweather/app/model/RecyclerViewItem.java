package com.simpleweather.app.model;

public class RecyclerViewItem {

	public static final int H_TYPE = 0; //view����0,header�����4��
    public static final int M_TYPE = 1; //view����2��middler�����1��
    public static final int I_TYPE = 2;//view ����3��item�����2��
	private int type;
	
    private int img;	//ͼ�꣬header�д�����������ͼ�꣬middler�д�����������ͼ��,item�д���ָ��ͼ��
    private String title;    //���⣬header�д��������¶ȣ�middler�д������ڣ�item�д���ָ������
    private String description;    //������header�д����������������middler�д����������������item�д���ָ�����

    private String temp1;//header�д���������ȼ���middler�д�������¶�
    private String temp2;//header�д�����ʱ�䣬middler�д�������¶�
    
    
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

	//getter �� setter

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
