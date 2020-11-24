package com.example.patolandis;

import android.net.Uri;

public class SliderItem {
    private Uri imagem;

    SliderItem(Uri imagens)
    {
        this.imagem = imagens;

    }
    public Uri getImagens()
    {
        return imagem;
    }
}
