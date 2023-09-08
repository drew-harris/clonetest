package main

import (
	"fmt"
	"log"
	"os"
	"strings"
	"time"

	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/credentials"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/s3/s3manager"
	"github.com/radovskyb/watcher"
)

func uploadFiles(uploader *s3manager.Uploader) {
	// Get first zip file in directory recurisvely
	path := "/backups/worlds/world"
	files, err := os.ReadDir(path)
	if err != nil {
		log.Fatal(err)
	}

	var zipFile string
	for _, f := range files {
		fmt.Println("File name: ", f.Name())
		if strings.Contains(f.Name(), ".zip") {
			zipFile = f.Name()
			break
		}
	}

	if zipFile == "" {
		fmt.Println("No zip file found")
		return
	}

	// // Rename file to backup.zip
	// err = os.Rename(path+"/"+zipFile, path+"/backup.zip")
	// if err != nil {
	// 	log.Fatal(err)
	// }
	// zipFile = "backup.zip"

	// Upload file to S3
	file, err := os.Open(path + "/" + zipFile)
	if err != nil {
		log.Fatal(err)
	}

	defer file.Close()

	upParams := &s3manager.UploadInput{
		Bucket: aws.String("minecraft-hgl-drew"),
		Key:    aws.String("backup.zip"),
		Body:   file,
	}

	fmt.Println("Uploading file to S3...")
	output, err := uploader.Upload(upParams)
	if err != nil {
		log.Fatal(err)
	} else {
		fmt.Println("File uploaded to S3 at", output.Location)
	}
}

func main() {
	w := watcher.New()

	s3Config := &aws.Config{
		Region:      aws.String("us-east-1"),
		Credentials: credentials.NewStaticCredentials(os.Getenv("KEY_ID"), os.Getenv("SECRET_KEY"), ""),
	}

	s3Session, err := session.NewSession(s3Config)
	if err != nil {
		fmt.Println("Could not sign in")
		fmt.Println(err.Error())
	}
	uploader := s3manager.NewUploader(s3Session)

	w.SetMaxEvents(1)

	w.FilterOps(watcher.Create)

	go func() {
		for {
			select {
			case event := <-w.Event:
				fmt.Println("File change detected")
				fmt.Println(event)
				fmt.Println()
				time.Sleep(time.Second * 5)
				uploadFiles(uploader)
			}
		}
	}()

	if err := w.AddRecursive("/backups"); err != nil {
		log.Fatalln(err)
	}

	fmt.Println("Watching files:")
	for path, f := range w.WatchedFiles() {
		fmt.Printf("%s: %s\n", path, f.Name())
	}

	// Start the watching process - it'll check for changes every 100ms.
	if err := w.Start(time.Millisecond * 900); err != nil {
		log.Fatalln(err)
	} else {
		fmt.Println("Watching files")
	}
}
