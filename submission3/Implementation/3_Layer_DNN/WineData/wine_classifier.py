from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import tensorflow as tf
import numpy as np

# Data sets
WINE_TRAINING = "wine_training.csv"
WINE_TEST = "wine_test.csv"

# Load datasets.
training_new = tf.contrib.learn.datasets.base.load_csv_with_header(
    filename=WINE_TRAINING,
    target_dtype=np.int,
    features_dtype=np.float32)
test_new = tf.contrib.learn.datasets.base.load_csv_with_header(
    filename=WINE_TEST,
    target_dtype=np.int,
    features_dtype=np.float32)

# Specify that all feature_columnss have real-value data
feature_columns = [tf.contrib.layers.real_valued_column("", dimension=13)]

# Build 3 layer DNN with 10, 20, 10 units respectively.
new_class = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns,
                                            hidden_units=[10, 20, 10],
                                            n_classes=3,
                                            model_dir="/tmp/run_again")

# Fit model.
new_class.fit(x=training_new.data,
               y=training_new.target,
               steps=500)

# Evaluate accuracy.
accuracy_score = new_class.evaluate(x=test_new.data,
                                     y=test_new.target)["accuracy"]
print('Accuracy: {0:f}'.format(accuracy_score))
