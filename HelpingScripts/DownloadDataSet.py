import os
import wget
import zipfile

url = "https://storage.googleapis.com/kaggle-data-sets/826739/1417276/bundle/archive.zip?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=gcp-kaggle-com%40kaggle-161607.iam.gserviceaccount.com%2F20221124%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20221124T195920Z&X-Goog-Expires=259200&X-Goog-SignedHeaders=host&X-Goog-Signature=26911db6adf66f89832e2c7c3d893952ada252ddee058aea8369f298f555ea6a787b4b763cb8d4a0303854ed705de8059db13552d017d551b514cd8d2253e05853158df0020c761cae080ebc773cfc2902c14c3047eecba86c730f4e0ee0c35fc960474d29a4c0d9f0c5a8a70d3d84b75da32c22ec5103a7e61446d1d33b398d895aaab816335b5fc830496dfcce1ed343c31a9cf23aa743560988b22ad92f2820ad42eeeb2cf64b15ba3f6de10bbfcee9878c5d720b4da911ee7df58afcc87a67d9252474b40f4a2d9aa30aebb4b190c7abc9a461275734940ff138dcc5e90e6ce6af5c31bb68b4b15bfc51b5c1cfd12893120a3934ca6d1e84c7613f3d9cb7"

if not os.path.isdir('data'):
    os.mkdir('data')

if not os.path.isfile('data/dataset.zip'):
    wget.download(url, 'data/dataset.zip')

if not os.path.isdir('data/dataset'):
    os.mkdir('data/dataset')

with zipfile.ZipFile('data/dataset.zip', 'r') as zip:
    zip.extractall('data/dataset')


