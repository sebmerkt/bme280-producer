package com.sensors;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.sql.Timestamp;

public class Bme280 {

    private static Long timestamp;
    private static double tempCelcius;
    private static double tempFahrenheit;
    private static double humidity;
    private static double pressure;

    static void measure() throws IOException {
        // Create I2C bus
        final I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        // Get I2C device, BME280 I2C address is 0x76(108)
        final I2CDevice device = bus.getDevice(0x77);

        // Read 24 bytes of data from address 0x88(136)
        final byte[] b1 = new byte[24];
            device.read(0x88, b1, 0, 24);

        // Convert the data
        // temp coefficients
        final int dig_T1 = (b1[0] & 0xFF) + ((b1[1] & 0xFF) * 256);
        final int dig_T2_val = (b1[2] & 0xFF) + ((b1[3] & 0xFF) * 256);
        final int dig_T2 = (dig_T2_val > 32767) ? dig_T2_val - 65536 : dig_T2_val;
//        if(dig_T2 > 32767)
//        {
//            dig_T2 -= 65536;
//        }
        final int dig_T3_val = (b1[4] & 0xFF) + ((b1[5] & 0xFF) * 256);
        final int dig_T3 = (dig_T3_val > 32767) ? dig_T3_val - 65536 : dig_T3_val;
//        if(dig_T3 > 32767)
//        {
//            dig_T3 -= 65536;
//        }

        // pressure coefficients
        final int dig_P1 = (b1[6] & 0xFF) + ((b1[7] & 0xFF) * 256);
        final int dig_P2_val = (b1[8] & 0xFF) + ((b1[9] & 0xFF) * 256);
        final int dig_P2 = (dig_P2_val > 32767) ? dig_P2_val - 65536 : dig_P2_val;
//        if(dig_P2 > 32767)
//        {
//            dig_P2 -= 65536;
//        }
        final int dig_P3_val = (b1[10] & 0xFF) + ((b1[11] & 0xFF) * 256);
        final int dig_P3 = (dig_P3_val > 32767) ? dig_P3_val - 65536 : dig_P3_val;
//        if(dig_P3 > 32767)
//        {
//            dig_P3 -= 65536;
//        }
        final int dig_P4_val = (b1[12] & 0xFF) + ((b1[13] & 0xFF) * 256);
        final int dig_P4 = (dig_P4_val > 32767) ? dig_P4_val - 65536 : dig_P4_val;
//        if(dig_P4 > 32767)
//        {
//            dig_P4 -= 65536;
//        }
        final int dig_P5_val = (b1[14] & 0xFF) + ((b1[15] & 0xFF) * 256);
        final int dig_P5 = (dig_P5_val > 32767) ? dig_P5_val - 65536 : dig_P5_val;
//        if(dig_P5 > 32767)
//        {
//            dig_P5 -= 65536;
//        }
        final int dig_P6_val = (b1[16] & 0xFF) + ((b1[17] & 0xFF) * 256);
        final int dig_P6 = (dig_P6_val > 32767) ? dig_P6_val - 65536 : dig_P6_val;
//        if(dig_P6 > 32767)
//        {
//            dig_P6 -= 65536;
//        }
        final int dig_P7_val = (b1[18] & 0xFF) + ((b1[19] & 0xFF) * 256);
        final int dig_P7 = (dig_P7_val > 32767) ? dig_P7_val - 65536 : dig_P7_val;
//        if(dig_P7 > 32767)
//        {
//            dig_P7 -= 65536;
//        }
        final int dig_P8_val = (b1[20] & 0xFF) + ((b1[21] & 0xFF) * 256);
        final int dig_P8 = (dig_P8_val > 32767) ? dig_P8_val - 65536 : dig_P8_val;
//        if(dig_P8 > 32767)
//        {
//            dig_P8 -= 65536;
//        }
        final int dig_P9_val = (b1[22] & 0xFF) + ((b1[23] & 0xFF) * 256);
        final int dig_P9 = (dig_P9_val > 32767) ? dig_P9_val - 65536 : dig_P9_val;
//        if(dig_P9 > 32767)
//        {
//            dig_P9 -= 65536;
//        }

        // Read 1 byte of data from address 0xA1(161)
        final int dig_H1 = ((byte)device.read(0xA1) & 0xFF);

        // Read 7 bytes of data from address 0xE1(225)
        device.read(0xE1, b1, 0, 7);

        // Convert the data
        // humidity coefficients
        final int dig_H2_val = (b1[0] & 0xFF) + (b1[1] * 256);
        final int dig_H2 = (dig_H2_val > 32767) ? dig_H2_val - 65536 : dig_H2_val;
//        if(dig_H2 > 32767)
//        {
//            dig_H2 -= 65536;
//        }
        final int dig_H3 = b1[2] & 0xFF ;
        final int dig_H4_val = ((b1[3] & 0xFF) * 16) + (b1[4] & 0xF);
        final int dig_H4 = (dig_H4_val > 32767) ? dig_H4_val - 65536 : dig_H4_val;
//        if(dig_H4 > 32767)
//        {
//            dig_H4 -= 65536;
//        }
        final int dig_H5_val = ((b1[4] & 0xFF) / 16) + ((b1[5] & 0xFF) * 16);
        final int dig_H5 = (dig_H5_val > 32767) ? dig_H5_val - 65536 : dig_H5_val;
//        if(dig_H5 > 32767)
//        {
//            dig_H5 -= 65536;
//        }
        final int dig_H6_val = b1[6] & 0xFF;
        final int dig_H6 = (dig_H6_val > 127) ? dig_H6_val - 256 : dig_H6_val;
//        if(dig_H6 > 127)
//        {
//            dig_H6 -= 256;
//        }

        // Select control humidity register
        // Humidity over sampling rate = 1
        device.write(0xF2 , (byte)0x01);
        // Select control measurement register
        // Normal mode, temp and pressure over sampling rate = 1
        device.write(0xF4 , (byte)0x27);
        // Select config register
        // Stand_by time = 1000 ms
        device.write(0xF5 , (byte)0xA0);

        // Read 8 bytes of data from address 0xF7(247)
        // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
        final byte[] data = new byte[8];
            device.read(0xF7, data, 0, 8);

        // Convert pressure and temperature data to 19-bits
        final long adc_p = (((long)(data[0] & 0xFF) * 65536) + ((long)(data[1] & 0xFF) * 256) + (long)(data[2] & 0xF0)) / 16;
        final long adc_t = (((long)(data[3] & 0xFF) * 65536) + ((long)(data[4] & 0xFF) * 256) + (long)(data[5] & 0xF0)) / 16;
        // Convert the humidity data
        final long adc_h = ((long)(data[6] & 0xFF) * 256 + (long)(data[7] & 0xFF));

        // Temperature offset calculations
        final double var1 = (((double)adc_t) / 16384.0 - ((double)dig_T1) / 1024.0) * ((double)dig_T2);
        final double var2 = ((((double)adc_t) / 131072.0 - ((double)dig_T1) / 8192.0) *
                (((double)adc_t)/131072.0 - ((double)dig_T1)/8192.0)) * ((double)dig_T3);
        final double t_fine = (long)(var1 + var2);
        tempCelcius = (var1 + var2) / 5120.0;
        tempFahrenheit = tempCelcius * 1.8 + 32;

        // Pressure offset calculations
        final double v1 = ((double)t_fine / 2.0) - 64000.0;
        final double v2 = v1 * v1 * ((double)dig_P6) / 32768.0;
        final double v3 = v2 + v1 * ((double)dig_P5) * 2.0;
        final double v4 = (v3 / 4.0) + (((double)dig_P4) * 65536.0);
        final double v5 = (((double) dig_P3) * v1 * v1 / 524288.0 + ((double) dig_P2) * v1) / 524288.0;
        final double v6 = (1.0 + v5 / 32768.0) * ((double)dig_P1);
        final double p = 1048576.0 - (double)adc_p;
        final double p1 = (p - (v4 / 4096.0)) * 6250.0 / v6;
        final double v7 = ((double) dig_P9) * p1 * p1 / 2147483648.0;
        final double v8 = p1 * ((double) dig_P8) / 32768.0;
        pressure = (p1 + (v7 + v8 + ((double)dig_P7)) / 16.0) / 100;

        // Humidity offset calculations
        final double var_H = (((double)t_fine) - 76800.0);
        final double var_H_1 = (adc_h - (dig_H4 * 64.0 + dig_H5 / 16384.0 * var_H)) * (dig_H2 / 65536.0 * (1.0 + dig_H6 / 67108864.0 * var_H * (1.0 + dig_H3 / 67108864.0 * var_H)));
        humidity = var_H_1 * (1.0 -  dig_H1 * var_H_1 / 524288.0);
        if(humidity > 100.0)
        {
            humidity = 100.0;
        }else
                if(humidity < 0.0)
        {
            humidity = 0.0;
        }

        timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    }

    static Long getTimestamp(){
        return timestamp;
    }

    static double getTempCelcius(){
        return tempCelcius;
    }

    static double getTempFahrenheit(){
        return tempFahrenheit;
    }

    static double getHumidity(){
        return humidity;
    }

    static double getPressure(){
        return pressure;
    }
}
