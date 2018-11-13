package com.example.sakshi.popularmoviesstage1.model;

public class Movie {



        private int id;
        private String posterPath;
        private String originalTitle;
        private String overview;
        private String releaseDate;
        private double voteAverage;



        public int getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }



        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public void setVoteAverage(double voteAverage){
            this.voteAverage=voteAverage;
        }
        public double getVoteAverage(){
            return voteAverage;
        }

    }

