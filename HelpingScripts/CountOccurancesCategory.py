import pandas as pd

data = pd.read_csv('data/other/meta_df.csv')

occurances = data['cat_name'].value_counts()
print(occurances)
