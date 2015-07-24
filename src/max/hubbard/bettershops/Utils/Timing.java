package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Shops.Items.ShopItem;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Timing {

    ShopItem it;
    int d = 0, h = 0, m = 0, s = 0;
    double dd = 0;
    boolean auto;
    long time = 0;
    boolean go = false;

    public Timing(ShopItem item, int d, int h, int m, int s, double dd, boolean auto) {
        it = item;
        this.d = d;
        this.h = h;
        this.m = m;
        this.s = s;
        this.dd = dd;

        this.auto = auto;
    }

    public Timing(ShopItem item, String s, boolean auto) {
        it = item;

        String[] split = s.split(Pattern.quote("|"));

        d = Integer.parseInt(split[0]);
        h = Integer.parseInt(split[1]);
        m = Integer.parseInt(split[2]);
        this.s = Integer.parseInt(split[3]);
        dd = Double.parseDouble(split[4]);
        time = Long.parseLong(split[5]);

        this.auto = auto;

        go = Boolean.valueOf(split[6]);
    }

    public void startTime() {
        time = Calendar.getInstance().getTime().getTime();
        go = true;
    }

    public void stop() {
        go = false;
    }

    public boolean update() {
        if (!go) return false;

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        c.add(Calendar.DATE, d);
        c.add(Calendar.HOUR, h);
        c.add(Calendar.MINUTE, m);
        c.add(Calendar.SECOND, s);

        Calendar b = Calendar.getInstance();

        return b.after(c);
    }

    public ShopItem getShopItem() {
        return it;
    }

    public int getDays() {
        return d;
    }

    public int getHours() {
        return h;
    }

    public int getMinutes() {
        return m;
    }

    public int getSeconds() {
        return s;
    }

    public void setDays(int d) {
        this.d = d;
    }

    public void setHours(int h) {
        this.h = h;
    }

    public void setMinutes(int m) {
        this.m = m;
    }

    public void setSeconds(int s) {
        this.s = s;
    }

    public void refreshTime() {
        time = Calendar.getInstance().getTime().getTime();
    }

    public void setObject(double dd) {
        this.dd = dd;
    }

    public double getObject() {
        return dd;
    }

    public int getD() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        c.add(Calendar.DATE, d);
        c.add(Calendar.HOUR, h);
        c.add(Calendar.MINUTE, m);
        c.add(Calendar.SECOND, s);

        Calendar b = Calendar.getInstance();

        Date dt2 = b.getTime();
        Date dt1 = c.getTime();

        long diff = dt2.getTime() - dt1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));

        return Math.abs(diffInDays);
    }

    public long getH() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        c.add(Calendar.DATE, d);
        c.add(Calendar.HOUR, h);
        c.add(Calendar.MINUTE, m);
        c.add(Calendar.SECOND, s);

        Calendar b = Calendar.getInstance();

        Date dt2 = b.getTime();
        Date dt1 = c.getTime();

        long diff = dt2.getTime() - dt1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));

        return Math.abs(diffHours);
    }

    public long getM() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        c.add(Calendar.DATE, d);
        c.add(Calendar.HOUR, h);
        c.add(Calendar.MINUTE, m);
        c.add(Calendar.SECOND, s);

        Calendar b = Calendar.getInstance();

        Date dt2 = b.getTime();
        Date dt1 = c.getTime();

        long diff = dt2.getTime() - dt1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));

        return Math.abs(diffMinutes);
    }

    public long getS() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        c.add(Calendar.DATE, d);
        c.add(Calendar.HOUR, h);
        c.add(Calendar.MINUTE, m);
        c.add(Calendar.SECOND, s);

        Calendar b = Calendar.getInstance();

        Date dt2 = b.getTime();
        Date dt1 = c.getTime();

        long diff = dt2.getTime() - dt1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));

        return Math.abs(diffSeconds);
    }

    public String getDifferenceString(){
        String s1 = "";
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        c.add(Calendar.DATE, d);
        c.add(Calendar.HOUR, h);
        c.add(Calendar.MINUTE, m);
        c.add(Calendar.SECOND, s);

        Calendar b = Calendar.getInstance();

        Date dt2 = b.getTime();
        Date dt1 = c.getTime();

        long diff = dt2.getTime() - dt1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));

        if (diffInDays != 0){
            s1 = s1 + "§7" + Math.abs(diffInDays) + " " + Language.getString("Timings","Day") + " ";
        }

        if (diffHours != 0){
            s1 = s1 + "§7" + Math.abs(diffHours) + " " + Language.getString("Timings","Hour") + " ";
        }

        if (diffMinutes != 0){
            s1 = s1 + "§7" + Math.abs(diffMinutes) + " " + Language.getString("Timings","Minute") + " ";
        }

        if (diffSeconds != 0){
            s1 = s1 + "§7" + Math.abs(diffSeconds) + " " + Language.getString("Timings","Second");
        }

        return s1;
    }

    @Override
    public String toString() {
        return d + "|" + h + "|" + m + "|" + s + "|" + dd + "|" + time + "|" + go;
    }
}
