/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kumanyaev.my_rent;

import java.io.Serializable;

/**
 *
 * @author 6PATyCb
 */
public class RentBean implements Serializable {

    private static final long serialVersionUID = 744572311L;
    private float tarifWaterHot = 0;
    private float prevousWaterHot = 0;
    private float currentWaterHot = 0;

    private float tarifWaterCold = 0;
    private float prevousWaterCold = 0;
    private float currentWaterCold = 0;

    private float tarifWaterOut = 0;
    private float prevousWaterOut = 0;
    private float currentWaterOut = 0;

    private float tarifElectro = 0;
    private float prevousElectro = 0;
    private float currentElectro = 0;

    private float tarifInet = 0;

    private float tarifMain = 0;

    private float prevChange = 0;

    private float factPay = 0;
    private boolean showMainSumm = true;

    /**
     * Пересчитаем водоотвод
     */
    private void updateWaterOut() {
        prevousWaterOut = prevousWaterHot + prevousWaterCold;
        currentWaterOut = currentWaterHot + currentWaterCold;
    }

    public static String format(float value) {
        return String.format("%.2f", value);
    }

    public float calculatePaymentValue() {
        float tarifSumm = 0;
        float hotDiff = currentWaterHot - prevousWaterHot;
        float hotResult = tarifWaterHot * hotDiff;
        tarifSumm += hotResult;
        float coldDiff = currentWaterCold - prevousWaterCold;
        float coldResult = tarifWaterCold * coldDiff;
        tarifSumm += coldResult;
        float waterOutDiff = currentWaterOut - prevousWaterOut;
        float waterOutResult = tarifWaterOut * waterOutDiff;
        tarifSumm += waterOutResult;
        float electroDiff = currentElectro - prevousElectro;
        float electroResult = tarifElectro * electroDiff;
        tarifSumm += electroResult;
        tarifSumm += tarifInet;
        float fullSumm = tarifMain + tarifSumm - prevChange;
        return fullSumm;
    }

    public String doAllCalculation() {
        StringBuilder sb = new StringBuilder();
        //sb.append("Расчет:\n");
        float tarifSumm = 0;
        float hotDiff = currentWaterHot - prevousWaterHot;
        float hotResult = tarifWaterHot * hotDiff;
        tarifSumm += hotResult;
        sb.append("Горячая вода: ").append(format(tarifWaterHot)).append("р x ").append(format(hotDiff)).append("куб = ").append(format(hotResult)).append("р\n");
        float coldDiff = currentWaterCold - prevousWaterCold;
        float coldResult = tarifWaterCold * coldDiff;
        tarifSumm += coldResult;
        sb.append("Холодная вода: ").append(format(tarifWaterCold)).append("р x ").append(format(coldDiff)).append("куб = ").append(format(coldResult)).append("р\n");
        float waterOutDiff = currentWaterOut - prevousWaterOut;
        float waterOutResult = tarifWaterOut * waterOutDiff;
        tarifSumm += waterOutResult;
        sb.append("Водоотвод: ").append(format(tarifWaterOut)).append("р x ").append(format(waterOutDiff)).append("куб = ").append(format(waterOutResult)).append("р\n");
        float electroDiff = currentElectro - prevousElectro;
        float electroResult = tarifElectro * electroDiff;
        tarifSumm += electroResult;
        sb.append("Электроэнергия: ").append(format(tarifElectro)).append("р x ").append(format(electroDiff)).append("кв = ").append(format(electroResult)).append("р\n");
        tarifSumm += tarifInet;
        if (tarifInet > 0) {
            sb.append("Интернет: ").append(format(tarifInet)).append("р\n");
        }
        sb.append("Сумма по тарифу: ").append(format(tarifSumm)).append("р\n");
        float fullSumm = tarifMain + tarifSumm - prevChange;
        if (showMainSumm) {
            sb.append("Основная сумма: ").append(format(tarifMain)).append("р\n");
            if (prevChange > 0) {
                sb.append("Остаток с прошлого месяца: ").append(format(prevChange)).append("р\n");
            }

            sb.append("Сумма всего: ").append(format(tarifSumm)).append("р + ").append(format(tarifMain));
            if (prevChange > 0) {
                sb.append("р - ").append(format(prevChange));
            }
            sb.append("р = ").append(format(fullSumm)).append("р\n");
            sb.append("Фактическая сумма оплаты: ").append(format(factPay)).append("р\n");
        }
        float change = factPay - fullSumm;
        if (change > 0) {
            sb.append("Переплата: ").append(format(change)).append("р");
        } else if (change < 0) {
            sb.append("Долг: ").append(format(change)).append("р");
        }
        return sb.toString();
    }

    public void currentToPrev() {
        prevChange = factPay - calculatePaymentValue();
        prevousWaterHot = currentWaterHot;
        prevousWaterCold = currentWaterCold;
        prevousElectro = currentElectro;
        updateWaterOut();

    }

    public float getTarifWaterHot() {
        return tarifWaterHot;
    }

    public void setTarifWaterHot(float tarifWaterHot) {
        this.tarifWaterHot = tarifWaterHot;
    }

    public float getPrevousWaterHot() {
        return prevousWaterHot;
    }

    public void setPrevousWaterHot(float prevousWaterHot) {
        this.prevousWaterHot = prevousWaterHot;
        updateWaterOut();
    }

    public float getCurrentWaterHot() {
        return currentWaterHot;
    }

    public void setCurrentWaterHot(float currentWaterHot) {
        this.currentWaterHot = currentWaterHot;
        updateWaterOut();
    }

    public float getTarifWaterCold() {
        return tarifWaterCold;
    }

    public void setTarifWaterCold(float tarifWaterCold) {
        this.tarifWaterCold = tarifWaterCold;
    }

    public float getPrevousWaterCold() {
        return prevousWaterCold;
    }

    public void setPrevousWaterCold(float prevousWaterCold) {
        this.prevousWaterCold = prevousWaterCold;
        updateWaterOut();
    }

    public float getCurrentWaterCold() {
        return currentWaterCold;
    }

    public void setCurrentWaterCold(float currentWaterCold) {
        this.currentWaterCold = currentWaterCold;
        updateWaterOut();
    }

    public float getTarifWaterOut() {
        return tarifWaterOut;
    }

    public void setTarifWaterOut(float tarifWaterOut) {
        this.tarifWaterOut = tarifWaterOut;
    }

    public float getPrevousWaterOut() {
        return prevousWaterOut;
    }

    public float getCurrentWaterOut() {
        return currentWaterOut;
    }

    public float getTarifElectro() {
        return tarifElectro;
    }

    public float getPrevousElectro() {
        return prevousElectro;
    }

    public float getCurrentElectro() {
        return currentElectro;
    }

    public void setTarifElectro(float tarifElectro) {
        this.tarifElectro = tarifElectro;
    }

    public void setPrevousElectro(float prevousElectro) {
        this.prevousElectro = prevousElectro;
    }

    public void setCurrentElectro(float currentElectro) {
        this.currentElectro = currentElectro;
    }

    public float getTarifInet() {
        return tarifInet;
    }

    public void setTarifInet(float tarifInet) {
        this.tarifInet = tarifInet;
    }

    public float getTarifMain() {
        return tarifMain;
    }

    public void setTarifMain(float tarifMain) {
        this.tarifMain = tarifMain;
    }

    public float getFactPay() {
        return factPay;
    }

    public void setFactPay(float factPay) {
        this.factPay = factPay;
    }

    public float getPrevChange() {
        return prevChange;
    }

    public void setPrevChange(float prevChange) {
        this.prevChange = prevChange;
    }

    public boolean isShowMainSumm() {
        return showMainSumm;
    }

    public void setShowMainSumm(boolean showMainSumm) {
        this.showMainSumm = showMainSumm;
    }

}
