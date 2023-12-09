import os, subprocess, click
from sys import exit

# Error handlers:
def is_docker_exists():
    try:
        subprocess.run(["docker", "--version"], check=True, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        return True
    except subprocess.CalledProcessError:
        print("Docker is not available. Please install Docker to continue")
        return exit(1)

def is_file_exists(file):
    return os.path.isfile(file)
# End of error handlers

is_docker_exists()

allowed_values = ["s", "save", "p", "push"]

while True:
    result = input("Do you want to save or push images [(S/s)save/(P/p)push]? ").lower()
    if result not in allowed_values:
        print("Only (S/s)save or (P/p)push allowed as valid choices. Please try again.")
    else:
        break

if result == "s" or result == "save":
    tar_name = input("Enter result archive name (without .tar): ")

file = input("Please provide file with docker images list: ")

if not is_file_exists(file):
    print(f"===\nTarget file was not found.\n===")
    exit(1)

images_list = open(f"{file}", "r")

source_repo = input("Specify source registry with project (for exmpl 'harbor.altezza.org/frisbee'): ")
target_repo = input("Specify source registry with project (for exmpl 'registry.msk.cht/tdm'): ")

class Images:

    def __init__(self, images):
        self.source_content = []
        self.target_content = []
        self.images = images

    def build_source_content(self):
        for i in self.images:
            self.source_content.append(i.strip('\n'))
        print(self.source_content)

    def pull_images(self):
        for i in self.source_content:
            os.system(f"docker pull {i}")
        return print(f"Images were pulled.")

    def tag_images(self):
        for i in self.source_content:
            self.target_content.append(i.replace(f'{source_repo}', f'{target_repo}'))
        print(self.target_content)
        if click.confirm(f"Are you agree with the result?", default=False):
            for i in range(len(self.source_content)):
                print(f"{self.source_content[i]} --> {self.target_content[i]}")
                os.system(f"docker tag {self.source_content[i]} {self.target_content[i]}")
            print("Images were tagged.")
        else:
            exit(1)
        return self.target_content

    def save_docker_images(self):
        images_list = " ".join(self.target_content)
        print("Start saving images...")
        os.system(f"docker save {images_list} > {tar_name}.tar")
        print("Images were saved. Please check it.")
        return images_list
    
    def push_images(self):
        for i in self.target_content:
            os.system(f"docker push {i}")
        print("Images were pushed. Please check it.")
        return True

# Object declaration:
docker_object = Images(images_list)

# Class methods:
docker_object.build_source_content()
docker_object.pull_images()
docker_object.tag_images()

if result == "p" or result == "push":
    docker_object.push_images()
else:
    docker_object.save_docker_images()

print("All the work is done. Congrats!")
