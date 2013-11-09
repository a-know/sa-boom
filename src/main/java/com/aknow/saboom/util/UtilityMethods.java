package com.aknow.saboom.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

@SuppressWarnings("deprecation")
public class UtilityMethods {

    public static final String ACTIVITY_OVER_CNT_LIST [] =
            {"1000000","900000","800000","700000","600000","500000","400000","300000","200000","100000","50000",
             "40000","30000","15000","10000","5000","2000","1000","500"};

    /**
     * 引数として受けたオブジェクトをblobstoreに書きこみ、そのkeyを返す。
     *
     * @param Object
     *            書き込み対象のオブジェクト
     * @return BlobKey
     *            blobstoreに書きこまれたファイルのblobkey
     * @throws IOException
     */
    public static BlobKey registBlob(Object o) throws IOException{

        //データをバイト配列に変換（圧縮も実施）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip_os = new ZipOutputStream(baos);

        zip_os.putNextEntry(new ZipEntry("zipped_entry"));

        ObjectOutputStream oos = new ObjectOutputStream(zip_os);
        oos.reset();
        oos.writeObject(o);
        oos.flush();
        zip_os.closeEntry();
        zip_os.close();
        baos.close();

        byte[] bytes = baos.toByteArray();

        // Get a file service
        FileService fileService = FileServiceFactory.getFileService();

        // Create a new Blob file with mime-type "application/octet-stream"
        AppEngineFile file = fileService.createNewBlobFile("application/octet-stream");

        // Open a channel to write to it
        boolean lock = true;
        FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);

        // This time we write to the channel using standard Java
        writeChannel.write(ByteBuffer.wrap(bytes));

        // Now finalize
        writeChannel.closeFinally();

        BlobKey key = fileService.getBlobKey(file);
        while(key == null || "".equals(key.toString())){
            key = fileService.getBlobKey(file);
        }
        return fileService.getBlobKey(file);
    }

    /**
     * 引数として受けたblobkeyでblobstore上のデータを削除する。
     *
     * @param BlobKey
     *            削除対象のblobkey
     * @return boolean
     *            false : 削除に失敗
     *            true  : 削除に成功
     */
    public static boolean deleteBlob(BlobKey blobKey){

        // Get a file service
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

        try{
            blobstoreService.delete(blobKey);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
