## Simple image gallery app

## General info
The gallery accepts only png files as input images. Image directories will be created automatically after running the app.
- path for the full images is C:\src\test\images\full
- path for the miniatures is C:\src\test\images\mini

## Setup
To run this project, please make sure that you have a locally created database using MySQL:

CREATE DATABASE IF NOT EXISTS gallery;

USE gallery;

CREATE TABLE images (

 imageid INT NOT NULL UNIQUE,
 
 imagename VARCHAR(255) NOT NULL
 
 );
