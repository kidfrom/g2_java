<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Insert</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
</head>
<body>
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-4">
                <div class="card mt-5">
                    <div class="card-header text-center">
                        Tambah Data
                    </div>
                    <form method="post" action="insert" class="card-body">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input class="form-control" type="text" name="username">
                        </div>
                        <div class="form-group">
                            <label for="fullname">Fullname</label>
                            <input class="form-control" type="text" name="fullname" >
                        </div>
                        <div class="form-group">
                            <label for="address">Address</label>
                            <input class="form-control" type="text" name="address">
                        </div>
                        <div class="form-group">
                            <label for="status">Status</label>
                            <input class="form-control" type="text" name="status">
                        </div>
                        <div class="form-group">
                            <label for="physics">Physics</label>
                            <input class="form-control" type="number" name="physics">
                        </div>
                        <div class="form-group">
                            <label for="calculus">Calculus</label>
                            <input class="form-control" type="number" name="calculus">
                        </div>
                        <div class="form-group">
                            <label for="biologi">Biologi</label>
                            <input class="form-control" type="number" name="biologi">
                        </div>
                        <button class="btn btn-primary btn-block">Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>