package ru.smirnovv.iris;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnovv.InternalServerException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * A class that manages encode data.
 * Класс, обеспечивающий шифрование данных.
 */
@Component
public class Cryptographer {
    /**
     * A class that manages encode data.
     * Класс, обеспечивающий шифрование данных.
     */
    private final Cipher ecipher;

    /**
     * A class that manages decode data.
     * Класс, обеспечивающий дешифрование данных.
     */
    private final Cipher dcipher;

    /**
     * Constructs an instance.
     * Конструктор без параметров.
     */
    @Autowired
    public Cryptographer() {
        try {
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();

            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception exception) {
            throw new InternalServerException(exception.getMessage());
        }
    }

    /**
     * Encodes data.
     * Шифрует данные.
     *
     * @param array byte array.
     *              массив байт.
     * @return encoded data.
     * Кодированные данные.
     */
    public byte[] encrypt(byte[] array) {
        try {
            return ecipher.doFinal(array);
        } catch (Exception exception) {
            throw new InternalServerException(exception.getMessage());
        }
    }

    /**
     * Decodes data.
     * Дешифрует данные.
     *
     * @param array byte array.
     *              массив байт.
     * @return decoded data.
     * Декодированные данные.
     */
    public byte[] decrypt(byte[] array) {
        try {
            return dcipher.doFinal(array);
        } catch (Exception exception) {
            throw new InternalServerException(exception.getMessage());
        }
    }
}
