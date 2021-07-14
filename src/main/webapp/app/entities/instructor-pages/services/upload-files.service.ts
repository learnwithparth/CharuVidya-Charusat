import { Injectable } from '@angular/core';
import * as AWS from 'aws-sdk/global';
import * as S3 from 'aws-sdk/clients/s3';

@Injectable({
  providedIn: 'root',
})
export class UploadFilesService {
  async uploadFile(file: File | null): Promise<string> {
    let fileName = '';

    if (file !== null) {
      const contentType = file.type;
      const bucket = new S3({
        accessKeyId: '',
        secretAccessKey: '',
        region: 'us-east-1',
      });
      fileName = '_' + Math.random().toString(36).substr(2, 9);
      const params = {
        Bucket: 'charuvidya1',
        Key: fileName,
        Body: file,
        ACL: 'public-read',
        ContentType: contentType,
      };

      try {
        const res = await bucket.upload(params).promise();
        fileName = res.Location;
      } catch (e) {
        window.alert(e.message);
      }

      //  await bucket.upload(params, function (err: any, data: any) {
      //   if (err) {
      //     window.alert('There was an error uploading your file: ');
      //     fileName = '';
      //     return false;
      //   }
      //   window.alert('Successfully uploaded file.');
      //   return true;
      // });

      //for upload progress
      // bucket.upload(params).on('httpUploadProgress', function (evt) {
      //   window.alert(`${evt.loaded} of ${evt.total} bytes`);
      // }).send(function (err: any, data: any) {
      //   if (err) {
      //     window.alert('There was an error uploading your file: ');
      //     return false;
      //   }
      //   window.alert('Successfully uploaded file.');
      //   return true;
      // });
    }

    return fileName;
  }
}
