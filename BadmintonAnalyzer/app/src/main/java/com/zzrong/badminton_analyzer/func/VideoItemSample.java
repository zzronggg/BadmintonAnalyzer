package com.zzrong.badminton_analyzer.func;

import java.util.ArrayList;

public class VideoItemSample {
    private static ArrayList<VideoItem> lst;
    private static ArrayList<String> lst2;

    public static ArrayList<VideoItem> getSample(){

        lst = new ArrayList<>();

        lst.add(new VideoItem("X8FGcXXDHK8",
                "JAK PODÁVAT v Badmintonu? Badminton s Adamem",
                "https://i.ytimg.com/vi/X8FGcXXDHK8/mqdefault.jpg",
                "Jak správně podat je častá otázka k diskuzi mezi dvěma parťáky, kteří se vydali na kurt. Máme pro tebe přehledně z jaké strany ..."));

        lst.add(new VideoItem("otUEAYgJHwQ",
                "Badminton training skills for beginners",
                "https://i.ytimg.com/vi/otUEAYgJHwQ/mqdefault.jpg",
                "Badminton training videos for beginners Badminton skill training for beginners badminton Speed and Strength training basic ..."));

        lst.add(new VideoItem("R8fSbx7Fcs0",
                "Punya Pasangan Baru, Bidadari Badminton Gronya Somerville Akhirnya Unjuk Gigi",
                "https://i.ytimg.com/vi/R8fSbx7Fcs0/mqdefault.jpg",
                "Atlet #olahraga #badminton Bidadari tepok bulu asal negeri Kanguru, Gronya Somerville punya pasangan baru loh. Eits…bukan ..."));

        lst.add(new VideoItem("jjgZhoy7BIo",
                "Maplestory BGM Compilation - Explorer's Journey to the Black Mage",
                "https://i.ytimg.com/vi/jjgZhoy7BIo/mqdefault.jpg",
                "I hope you enjoy this Maplestory BGM compilation as we follow an Explorer's journey to defeat the Black Mage! :) Credits go to Nexon, StudioEIM, Asteria, Necord and the respective artists for all the beautiful art and tunes within this video."));

        lst.add(new VideoItem("pCvnLww5dVs",
                "keseruan Alshad ricistr dan artis lainnya main badminton,Fadil semangati alshad",
                "https://i.ytimg.com/vi/pCvnLww5dVs/mqdefault.jpg",
                "Feeding exercises is an important part of improving footwork, reaction speed, and quality returns. This 1-minute badminton drill ..."));

        lst.add(new VideoItem("K6ENtpMLHzg",
                "Son Wan ho vs Tian Houwei | Badminton Highlights | 손완호 | 田厚威",
                "https://i.ytimg.com/vi/K6ENtpMLHzg/mqdefault.jpg",
                "alshadahmad #ricistr#fadiljaidi #kevinsanjaya #boywiliam."));

        lst.add(new VideoItem("7V3KwFrzKwc",
                "Top 10 Young badminton Players | The Future of Men&#39;s Singles",
                "https://i.ytimg.com/vi/7V3KwFrzKwc/mqdefault.jpg",
                "Watch the highlights of this amazing match between Son Wan-ho (손완호) and Tian Houwei (田厚威). With the Smart Badminton ..."));

        lst.add(new VideoItem("id887JoTRyI",
                "Badminton Asia Championships 2022 Badminton Draw - BAC 2022 Badminton -OPEN/MASTERS",
                "https://i.ytimg.com/vi/id887JoTRyI/mqdefault.jpg",
                "Rising star of Men's singles We have created a list of 10 young badminton players and they will become the top 10 of the world of ..."));

        lst.add(new VideoItem("JAvYggRQLlY",
                "Nghịch ngợm tí#cầulông #badminton #girls #badminton #shorts",
                "https://i.ytimg.com/vi/id887JoTRyI/mqdefault.jpg",
                " Badminton Asia Championships 2022 Badminton Draw - BAC 2022 Badminton -OPEN/MASTERS #bacbadminton2022 ..."));

        return lst;

    }

    public static ArrayList<VideoItem> rctlySample(){

        lst = new ArrayList<>();

        lst.add(new VideoItem("n8JTt1yTXAE",
                "TotalEnergies BWF World Championships 2021 _ Chou Tien Chen (TPE) [4] vs Lu Guang Zu (CHN) _ R32",
                "https://i.ytimg.com/vi/n8JTt1yTXAE/hqdefault.jpg","d"));
        lst.add(new VideoItem("szSWLFTR-uQ",
                "YONEX All England Open 2022 _ Chou Tien Chen (TPE) [4] vs Jonatan Christie (INA) [7] _ Quarterfinals",
                "https://i.ytimg.com/vi/szSWLFTR-uQ/hqdefault.jpg","e"));
        lst.add(new VideoItem("l949llHooC",
                "YONEX All England Open 2022 _ Viktor Axelsen (DEN) [1] vs Chou Tien Chen (TPE) [4] _ Semifinals",
                "https://i.ytimg.com/vi/l949llHooCQ/hqdefault.jpg","f"));

        return lst;
    }

    public static ArrayList<String> dataFragSample(){
        lst2 = new ArrayList<>();
        //title;score;classify;game-date;upload-date
        lst2.add("YONEX All England 2022YONEX All England 2022YONEX All England 2022YONEX All Eng");
        lst2.add("21：10 / 21：15");
        lst2.add("周天成");
        lst2.add("2022/03/19");
        lst2.add("2022/05/20");

        return lst2;
    }

    public static ArrayList<String> sectFragSample(){
        lst2 = new ArrayList<>();
        lst2.add("00:10~00:23");
        lst2.add("00:26~00:50");
        lst2.add("00:55~01:08");
        lst2.add("01:11~01:30");
        lst2.add("02:47~02:58");
        lst2.add("03:11~03:27");
        lst2.add("03:31~03:40");
        lst2.add("03:45~04:00");

        return lst2;
    }

    public static ArrayList<String> analFragSample(){
        //undefined
        return lst2;
    }


}
