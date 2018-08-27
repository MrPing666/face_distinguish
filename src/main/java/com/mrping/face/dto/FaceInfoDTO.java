package com.mrping.face.dto;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public class FaceInfoDTO {

    /**颜值游戏记录ID**/
    private Long gameId;

    /**测颜值**/
    private FaceBeauty beauty;

    /**相似度**/
    private Integer score;
    /**明星指数**/
    private Integer starIndex;
    /**明星信息**/
    private String userInfo;
    /**明星图片地址**/
    private String imageUrl;
    /**百分比(夫妻相)**/
    private String exceedRatio;

    /**水印图片地址**/
    private String imgUrl;
    /**带二维码分享图片地址**/
    private String shareImgUrl;

    public FaceInfoDTO() {}

    public FaceInfoDTO(Integer score, String beauty) {
        this.score = score;
        this.beauty = FaceBeauty.checkYanzhi(beauty);
    }

    /**
     * 测夫妻相
     * @param score
     */
    public FaceInfoDTO(String score) {
        this.score = Integer.parseInt(score.split("\\.")[0]);
        this.exceedRatio = getRatio();
    }

    /**
     * 测明星脸
     * @param score
     * @param userInfo
     */
    public FaceInfoDTO(String score, String userInfo, Long starId) {

        this.score = Integer.parseInt(score.split("\\.")[0]);
        this.userInfo = userInfo;
        this.starIndex = this.score % 20 != 0 ? this.score / 20 + 1 : this.score / 20;
//        Star star = Registry.queryBean(StarRpt.class).getStarById(starId);
//        if(star != null) this.imageUrl = star.getNewUrl();

    }

    /**
     * 获取百分比
     */
    public String getRatio(){

        int a1 = 1;int b1 = 10;
        int a2 = 11;int b2 = 20;
        int a3 = 21;int b3 = 35;
        int a4 = 36;int b4 = 45;
        int a5 = 46;int b5 = 55;
        int a6 = 56;int b6 = 70;
        int a7 = 71;int b7 = 80;
        int a8 = 81;int b8 = 100;
        String ratio = "0%";
        if(score >= a1 && score <= b1){
            ratio = "12%";
        }else if(score >= a2 && score <= b2){
            ratio = "25%";
        }else if(score >= a3 && score <= b3){
            ratio = "37%";
        }else if(score >= a4 && score <= b4){
            ratio = "48%";
        }else if(score >= a5 && score <= b5){
            ratio = "59%";
        }else if(score >= a6 && score <= b6){
            ratio = "73%";
        }else if(score >= a7 && score <= b7){
            ratio = "85%";
        }else if(score >= a8 && score < b8){
            ratio = "99%";
        }else if(score == b8){
            ratio = "100%";
        }
        return ratio;
    }

    public void saveGameId(Long gameId){
        this.gameId = gameId;
    }

    public void saveImgUrl(String waterMarkImgUrl, String shareImgUrl){
        this.imgUrl = waterMarkImgUrl;
        this.shareImgUrl = shareImgUrl;
    }

    public Long getGameId() {
        return gameId;
    }

    public FaceBeauty getBeauty() {
        return beauty;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getStarIndex() {
        return starIndex;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getExceedRatio() {
        return exceedRatio;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getShareImgUrl() {
        return shareImgUrl;
    }

}
