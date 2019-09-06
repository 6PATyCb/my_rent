/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kumanyaev.my_rent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileLock;

/**
 *
 * @author 6PATyCb
 */
public abstract class Serializer {

    private static final String FILE_NAME = "rentBean.save";

    /**
     * Сериализация объекта
     *
     * @param bean
     */
    public static final void serialize(RentBean bean) {
        if (bean == null) {
            throw new IllegalArgumentException();
        }
        try {
            File file = new File(FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            FileLock fLock = fos.getChannel().lock();
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(fos);
                try {
                    outputStream.writeObject(bean);
                } finally {
                    fLock.release();
                    outputStream.close();
                }
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
    }

    /**
     * Десериализация объекта
     *
     * @param ifExceptionBean бин, возвращаемый, если не удалась десериализация
     * @return
     */
    @SuppressWarnings("SleepWhileInLoop")
    public static final RentBean deserialize(RentBean ifExceptionBean) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                return ifExceptionBean;
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(FILE_NAME));
            int tryReadCounter = 0;
            ObjectInputStream in = null;
            try {
                //т.к. мы блокируем файл на уровне ОС при записи, теоретически, при чтении,
                //мы можем попадать на блокировку, которая вызывает IOException,
                //поэтому сделаем несколько попыток считать, перед выбросом ошибки
                int MAX_READ_FILE_TRIES = 3;
                long MILLISEC_BETWEEN_READ_FILE_TRIES = 1000;
                while (tryReadCounter < MAX_READ_FILE_TRIES) {
                    try {
                        in = new ObjectInputStream(bufferedInputStream);
                        return (RentBean) in.readObject();
                    } catch (IOException skipped) {
                        tryReadCounter++;
                        Thread.sleep(MILLISEC_BETWEEN_READ_FILE_TRIES);
                    }
                }
                throw new Exception("Не удалось прочитать файл");
            } finally {
                bufferedInputStream.close();
                if (in != null) {
                    in.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return ifExceptionBean;
        }
    }
}
