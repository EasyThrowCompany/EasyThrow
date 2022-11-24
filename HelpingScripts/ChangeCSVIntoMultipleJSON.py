import json
import os
import pandas as pd
import numpy as np
import pathlib

objects = pd.read_csv('data/other/meta_df.csv')
conversion = pd.read_csv('data/other/category_conversion.csv')

# Drop unnecessery categories
objects = objects.drop('img_id', axis=1)
objects = objects.drop('cat_id', axis=1)
objects = objects.drop('supercategory', axis=1)
objects = objects.drop('ann_id', axis=1)
objects = objects.drop('area', axis=1)

print(objects)
print(conversion)

# Replace old categories with new one (From loaded csv)
for index, row in conversion.iterrows():
    objects.loc[objects['cat_name'] == row['old_category'],
                'cat_name'] = row['new_category']

# Remove objects with unwanted category
objects = objects[objects['cat_name'] != 'ToDelete']

# Reset indexes and remove new additional column
objects = objects.reset_index()
objects = objects.drop('index', axis=1)

# Rename columns x -> x1, y -> y1
objects.rename(columns={'x':'x1','y':'y1'}, inplace=True)

# Create new columns x2, y2
objects['x2'] = objects['x1'] + objects['width']
objects['y2'] = objects['y1'] + objects['height']

# Drop columns width and height
objects.drop('width', axis=1, inplace=True)
objects.drop('height', axis=1, inplace=True)

# Create new column format
objects['format'] = np.nan
for index, row in objects.iterrows():
    tempString = pathlib.Path(row['img_file']).suffix
    tempString = tempString[1: ]
    objects['format'] = tempString

# Rename columns
objects.rename(columns={'img_file': 'image_url'}, inplace=True)

print(objects)

# Save every image to different JSON file
jsondata = {}
i = 0
length = objects.shape[0]
while i < length:
    jsondata = {}
    image_details = {}
    label = []

    tempRow = objects.iloc[i]
    # print(tempRow['image_url'])

    image_url = tempRow['image_url']
    image_details['format'] = tempRow['format']
    image_details['width'] = str(tempRow['img_width'])
    image_details['height'] = str(tempRow['img_height'])

    while i < length and image_url == objects.iloc[i]['image_url']:
        tempLabel = {}
        tempRow2 = objects.iloc[i]
        tempLabel['label'] = tempRow2['cat_name']
        tempLabel['topX'] = str(tempRow2['x1'])
        tempLabel['topY'] = str(tempRow2['y1'])
        tempLabel['bottomX'] = str(tempRow2['x2'])
        tempLabel['bottomY'] = str(tempRow2['y2'])
        tempLabel['isCrowd'] = str(0)
        label.append(tempLabel)
        i = i+1

    jsondata['image_url'] = image_url
    jsondata['image_details'] = image_details
    jsondata['label'] = label
    # print(jsondata)
    print(json.dumps(jsondata, indent=4))

    jsonAll = json.dumps(jsondata)
    head, tail = os.path.split(image_url)
    staticPathDirectory = 'data/JSONs/'
    directory = staticPathDirectory + head

    if(not os.path.isdir(directory)):
        os.mkdir(directory)

    pathname, extension = os.path.splitext(image_url)
    path = staticPathDirectory + pathname + '.json'
    file = open(path, 'w')
    json.dump(jsondata, file, indent=4)





# Group up objects by image_url

