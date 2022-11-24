import pandas as pd

data = pd.read_csv('data/meta_df.csv')

occurances = data['supercategory'].value_counts()
print(occurances)
