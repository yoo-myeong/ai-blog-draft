package com.kotlin.aiblogdraft.api.image

import com.kotlin.aiblogdraft.cloud.image.S3Uploader
import io.mockk.mockk
import org.springframework.web.multipart.MultipartFile

class S3UploaderStub : S3Uploader(mockk()) {
    override fun upload(files: Array<MultipartFile>): List<String> =
        files.mapIndexed {
                index,
                _,
            ->
            "https://postfiles.pstatic.net/MjAyNDA5MjBfMTEw/MDAxNzI2ODQxMzEzMDc4.TXxJtCS4xhWIY_uBNMNP3JnHhFFFCuVyQCOfrknwVK8g._TE0s1pDBVHzyfq2_Wd6PtR3LV_0erYA6mzSG1a21HYg.JPEG/IMG_$index.jpeg"
        }
}
