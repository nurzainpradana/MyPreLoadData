package com.nurzainpradana.androidfundamental.mypreloaddata.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MahasiswaModel implements Parcelable {
    private int id;
    private String name;
    private String nim;

    //constructor
    public MahasiswaModel() {
    }

    protected MahasiswaModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.nim = in.readString();
    }

    public static final Creator<MahasiswaModel> CREATOR = new Creator<MahasiswaModel>() {
        @Override
        public MahasiswaModel createFromParcel(Parcel in) {
            return new MahasiswaModel(in);
        }

        @Override
        public MahasiswaModel[] newArray(int size) {
            return new MahasiswaModel[size];
        }
    };

    //Getter and Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.nim);
    }
}
