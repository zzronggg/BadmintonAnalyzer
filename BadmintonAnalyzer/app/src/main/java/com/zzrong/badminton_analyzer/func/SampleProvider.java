package com.zzrong.badminton_analyzer.func;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleProvider {

    public static ArrayList<VideoItem> getSample(){

        ArrayList<VideoItem> lst = new ArrayList<>();

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

        ArrayList<VideoItem> lst = new ArrayList<>();

        lst.add(new VideoItem("n8JTt1yTXAE",
                "TotalEnergies BWF World Championships 2021 _ Chou Tien Chen (TPE) [4] vs Lu Guang Zu (CHN) _ R32",
                "https://i.ytimg.com/vi/n8JTt1yTXAE/hqdefault.jpg","d"));
        lst.add(new VideoItem("szSWLFTR-uQ",
                "YONEX All England Open 2022 _ Chou Tien Chen (TPE) [4] vs Jonatan Christie (INA) [7] _ Quarterfinals",
                "https://i.ytimg.com/vi/szSWLFTR-uQ/hqdefault.jpg","e"));
        lst.add(new VideoItem("l949llHooCQ",
                "YONEX All England Open 2022 _ Viktor Axelsen (DEN) [1] vs Chou Tien Chen (TPE) [4] _ Semifinals",
                "https://i.ytimg.com/vi/l949llHooCQ/hqdefault.jpg","f"));

        return lst;
    }

    public static ArrayList<String> dataFragSample(){
        ArrayList<String> lst = new ArrayList<>();
        //title:0
        lst.add("YONEX All England 2022YONEX All England 2022YONEX All England 2022YONEX All Eng");
        //analyzed date:1
        lst.add("2022/03/19");
        //score:2~7
        lst.add("21");
        lst.add("10");
        lst.add("16");
        lst.add("21");
        lst.add("");  //只打兩局
        lst.add("");
        return lst;
    }

    public static ArrayList<List<Object>> sectFragSample(){
        //ArrayList:List:(String, boolean)
        //true: 藍方得分  false:紅方得分
        ArrayList<List<Object>> lst = new ArrayList<>();
        lst.add(Arrays.asList("1：0", true));
        lst.add(Arrays.asList("2：0",true));
        lst.add(Arrays.asList("2：1",false));
        lst.add(Arrays.asList("3：1",true));
        lst.add(Arrays.asList("3：2",false));
        lst.add(Arrays.asList("3：3",false));
        lst.add(Arrays.asList("3：4",false));
        lst.add(Arrays.asList("3：5",false));
        lst.add(Arrays.asList("3：6",false));
        return lst;
    }

    public static ArrayList<String> predFragSampleUp(){
        //undefined
        ArrayList<String> lst = new ArrayList<>();
        //0：發球  1：平球  2：小球  3：挑球  4：切球  5：殺球  10：得分
        lst.add("2");
        lst.add("5");
        lst.add("5");
        lst.add("2");
        lst.add("4");
        lst.add("5");
        lst.add("3");
        lst.add("3");
        lst.add("2");
        lst.add("3");
        lst.add("3");
        lst.add("3");
        return lst;
    }
    public static ArrayList<String> predFragSampleDown(){
        //undefined
        ArrayList<String> lst = new ArrayList<>();
        //0：發球  1：平球  2：小球  3：挑球  4：切球  5：殺球  10：得分
        lst.add("3");
        lst.add("3");
        lst.add("2");
        lst.add("3");
        lst.add("3");
        lst.add("2");
        lst.add("5");
        lst.add("5");
        lst.add("2");
        lst.add("4");
        lst.add("5");
        lst.add("5");
        return lst;
    }

    public static ArrayList<String> statFragSample(){
        //undefined
        ArrayList<String> lst = new ArrayList<>();
        lst.add("Winner");
        return lst;
    }

    public static ArrayList<String> bookmarkSample(){
        ArrayList<String> lst2 = new ArrayList<>();
        lst2.add("周眯眯");
        return lst2;
    }

    public static String totalInfoSample(){
        String str = "{'games': [{'score count': 3, 'top bot score': [19, 21]}, {'score count': 5, 'top bot score': [15, 22]}],\n" +
                "\n" +
                " 'blue_total_shots': {'長球': 14.13, '切球': 26.36, '殺球': 8.7, '挑球': 33.42, '小球': 8.15, '平球': 2.72, '撲球': 6.52, '拉吊': 0.0, '搶攻': 0.0},\n" +
                "\n" +
                " 'red_total_shots': {'長球': 17.59, '切球': 18.64, '殺球': 11.02, '挑球': 31.76, '小球': 8.14, '平球': 2.89, '撲球': 9.97, '拉吊': 0.0, '搶攻': 0.0},\n" +
                "\n" +
                " 'blue_win_shots': {'長球': 42.31, '切球': 46.39, '殺球': 59.38, '挑球': 41.46, '小球': 43.33, '平球': 20.0, '撲球': 45.83, '拉吊': 0, '搶攻': 0},\n" +
                "\n" +
                " 'red_win_shots': {'長球': 53.73, '切球': 53.52, '殺球': 69.05, '挑球': 50.41, '小球': 64.52, '平球': 36.36, '撲球': 55.26, '拉吊': 0, '搶攻': 0}, 'red loss shots': {'長球': 18.02, '切球': 19.19, '殺球': 7.56, '挑球': 34.88, '小球': 6.4, '平球': 4.07, '撲球': 9.88, '拉吊': 0.0, '搶攻': 0.0},\n" +
                "\n" +
                " 'blue_total_move': {'DLBL': 0.79, 'DLBR': 1.05, 'DLFL': 1.57, 'DLFR': 1.57, 'DSBL': 1.57, 'DSBR': 2.36, 'DSFL': 9.19, 'DSFR': 6.3, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 3.94, 'LSF': 17.32, 'TLL': 17.85, 'TLR': 0.79, 'TSL': 0.0, 'TSR': 9.97, 'NM': 25.72},\n" +
                "\n" +
                " 'red_total_move': {'DLBL': 1.63, 'DLBR': 2.99, 'DLFL': 1.36, 'DLFR': 2.72, 'DSBL': 3.53, 'DSBR': 5.16, 'DSFL': 9.51, 'DSFR': 7.07, 'LLB': 0.27, 'LLF': 0.54, 'LSB': 3.8, 'LSF': 16.3, 'TLL': 10.05, 'TLR': 1.09, 'TSL': 0.0, 'TSR': 10.87, 'NM': 23.1}\n" +
                "\n" +
                "}";

//        Log.d("JSONSAMPLE: ",str);
        return str;
    }

public static String sectionInfoSample(){
        String str = "[\n" +
                "      {'game': 1, 'top bot score': [1, 0], 'winner': true, 'blue serve first': false, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 1, 'top bot score': [1, 1], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 1, 'top bot score': [1, 2], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 1, 'top bot score': [1, 3], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 1, 'top bot score': [1, 4], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 1, 'top bot score': [1, 5], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 2, 'top bot score': [1, 6], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 2, 'top bot score': [1, 7], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      },\n" +
                "    \n" +
                "     {'game': 2, 'top bot score': [1, 8], 'winner': false, 'blue serve first': true, \n" +
                "    \n" +
                "       'shot list': [['↑ 挑球', 0, 4, false], ['↓ 長球', 4, 13, true], ['↑ 長球', 13, 18, false], ['↓ 殺球', 18, 29, true], ['↑ 挑球', 29, 35, false], ['↓ 切球', 35, 43, true], ['↑ 挑球', 43, 53, false], ['↓ 殺球', 53, 56, true], ['↑ 挑球', 56, 59, false]],\n" +
                "        \n" +
                "       'blue shot dict': {'長球': 1, '切球': 1, '殺球': 2, '挑球': 0, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'red shot dict': {'長球': 1, '切球': 0, '殺球': 0, '挑球': 4, '小球': 0, '平球': 0, '撲球': 0}, \n" +
                "    \n" +
                "       'move direction list': [['NM', true], ['DSBR', false], ['NM', true], ['TLL', false], ['NM', true], ['DSFR', false], ['TLR', true], ['NM', false], ['NM', true]], \n" +
                "        \n" +
                "       'blue move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 0.0, 'DSFL': 0.0, 'DSFR': 0.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 0.0, 'TLR': 20.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 80.0}, \n" +
                "    \n" +
                "       'red move dict': {'DLBL': 0.0, 'DLBR': 0.0, 'DLFL': 0.0, 'DLFR': 0.0, 'DSBL': 0.0, 'DSBR': 25.0, 'DSFL': 0.0, 'DSFR': 25.0, 'LLB': 0.0, 'LLF': 0.0, 'LSB': 0.0, 'LSF': 0.0, 'TLL': 25.0, 'TLR': 0.0, 'TSL': 0.0, 'TSR': 0.0, 'NM': 25.0}\n" +
                "      }\n" +
                "    \n" +
                "    ]";

//        Log.d("JSONSAMPLE: ",str);
        return str;
    }


}
