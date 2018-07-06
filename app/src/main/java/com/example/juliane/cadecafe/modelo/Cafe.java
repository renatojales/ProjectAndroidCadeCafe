package com.example.juliane.cadecafe.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Blob;
import java.util.List;

public class Cafe implements Parcelable {
    private String nome;
    private String endereco;
    private String telefone;
    private String device;
    private String profileImage;



    public Cafe(String nome, String telefone, String device, String endereco, String profileImage){
        this.nome = nome;
        this.telefone = telefone;
        this.device = device;
        this.endereco = endereco;
        this.profileImage = profileImage;




    }

    protected Cafe(Parcel in) {
        nome = in.readString();
        telefone = in.readString();
        device = in.readString();
        endereco = in.readString();
        profileImage = in.readString();
    }

    public static final Creator<Cafe> CREATOR = new Creator<Cafe>() {
        @Override
        public Cafe createFromParcel(Parcel in) {
            return new Cafe(in);
        }

        @Override
        public Cafe[] newArray(int size) {
            return new Cafe[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDevice() { return device; }

    public void setDevice(String device) { this.device = device; }

    @Override
    public String toString() {
        return "Cafe{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", device='" + device + '\'' +
                ", endereco='" + endereco + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(endereco);
        dest.writeString(profileImage);
        dest.writeString(telefone);
        dest.writeString(device);
    }
}

