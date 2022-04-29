package com.mxarcher.biue.models;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/29 16:11
 * @Description:
 */
public class SignalData {
    @Override
    public String toString() {
        return "SignalData{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", ppg=" + ppg +
                ", temp=" + temp +
                ", resistance=" + resistance +
                '}';
    }

    public short x;
    public short y;
    public short z;
    public short ppg;
    public short temp;
    public short resistance;

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public short getZ() {
        return z;
    }

    public void setZ(short z) {
        this.z = z;
    }

    public short getPpg() {
        return ppg;
    }

    public void setPpg(short ppg) {
        this.ppg = ppg;
    }

    public short getTemp() {
        return temp;
    }

    public void setTemp(short temp) {
        this.temp = temp;
    }

    public short getResistance() {
        return resistance;
    }

    public void setResistance(short resistance) {
        this.resistance = resistance;
    }
}
