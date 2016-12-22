package com.ecs160.nittacraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLineDataSource {
    protected BufferedReader DBufferedReader;

    public CLineDataSource(CDataSource source) {
        DBufferedReader = new BufferedReader(new InputStreamReader(source.DInputStream));
    }

    public boolean Read(StringBuffer readLine) {
        readLine.delete(0, readLine.capacity());
        try {
            readLine.append(DBufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}



