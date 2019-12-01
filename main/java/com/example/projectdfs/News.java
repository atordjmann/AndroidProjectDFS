package com.example.projectdfs;

public class News {
    private String titre;
    private String auteur;
    private String date;
    private String image;

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitre(){
        return titre;
    }

    public void setTitre(String titre){
        this.titre = titre;
    }

    public News(String titre, String auteur, String date, String image){
        this.titre = titre;
        this.auteur = auteur;
        this.date = date;
        this.image = image;
    }
}
