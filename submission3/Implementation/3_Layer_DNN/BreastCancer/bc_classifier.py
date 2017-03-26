from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import tensorflow as tf
import numpy as np

# Data sets
BC_TRAINING = "bc_training.csv"
BC_TEST = "bc_test.csv"

# Load datasets.
training = tf.contrib.learn.datasets.base.load_csv_with_header(
    filename=BC_TRAINING,
    target_dtype=np.int,
    features_dtype=np.float32)
test = tf.contrib.learn.datasets.base.load_csv_with_header(
    filename=BC_TEST,
    target_dtype=np.int,
    features_dtype=np.float32)

# Specify that all feature_columnss have real-value data
feature_columns = [tf.contrib.layers.real_valued_column("", dimension=30)]

# Build 3 layer DNN with 10, 20, 10 units respectively.
new_classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns,
                                            hidden_units=[10, 20, 10],
                                            n_classes=2,
                                            model_dir="/tmp/bc_model")

# Fit model.
new_classifier.fit(x=training.data,
               y=training.target,
               steps=2000)

# Evaluate accuracy.
accuracy_score = new_classifier.evaluate(x=test.data,
                                     y=test.target)["accuracy"]
print('Accuracy: {0:f}'.format(accuracy_score))

