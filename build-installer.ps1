param(
    [switch]$Publish
)

$ErrorActionPreference = "Stop"

$AppName = "Calculator"
$Version = (mvn help:evaluate `
    -Dexpression=project.version `
    -q `
    -DforceStdout).Trim()

if ($LASTEXITCODE -ne 0 or [string]::IsNullOrWhiteSpace($Version)) {

    throw "Could not determine project version from pom.xml."
}

$ArtifactId = (mvn help:evaluate `
    -Dexpression=project.artifactId `
    -q `
    -DforceStdout).Trim()

if ($LASTEXITCODE -ne 0 or [string]::IsNullOrWhiteSpace($ArtifactId)) {

    throw "Could not determine artifact ID from pom.xml."
}

$MainJar = "$ArtifactId-$Version.jar"
$Icon = "src\main\resources\icons\calculator.ico"
$DependencyDir = "target\dependency"
$PackageInput = "target\package-input"
$PackageDir = "target\package"
$InstallerDir = "target\installer"
$Repo = "theDollar013/calculator"
$Tag = "v$Version"

if ($Publish) {

    Write-Host "=== Checking Git Status ==="

    $GitStatus = git status --porcelain

    if ($LASTEXITCODE -ne 0) {

        throw "Could not determine Git repository status."
    }

    if ($GitStatus) {

        Write-Host $GitStatus
        throw "Uncommitted changes detected. Commit or discard changes before publishing."
    }

    Write-Host "Working tree is clean."

    Write-Host "=== Checking GitHub Release Tag ==="

    gh release view $Tag --repo $Repo *> $null

    if ($LASTEXITCODE -eq 0) {

        throw "GitHub release $Tag already exists. Update the version in pom.xml before publishing."
    }
}

Write-Host "=== Calculator Release $Version ==="
Write-Host "=== Building Calculator ==="

mvn clean verify
if ($LASTEXITCODE -ne 0) {
    throw "Maven build failed."
}

Write-Host "=== Copying runtime dependencies ==="

mvn dependency:copy-dependencies `
    -DincludeScope=runtime

if ($LASTEXITCODE -ne 0) {
    throw "Dependency copy failed."
}

Write-Host "=== Preparing jpackage input ==="

New-Item -ItemType Directory -Force -Path $PackageInput | Out-Null

Copy-Item "target\$MainJar" $PackageInput
Copy-Item "$DependencyDir\*.jar" $PackageInput

Write-Host "=== Creating application image ==="

jpackage `
    --type app-image `
    --name $AppName `
    --app-version $Version `
    --vendor "theDollar013" `
    --description "JavaFX calculator with basic mathematical functions" `
    --icon $Icon `
    --input $PackageInput `
    --main-jar $MainJar `
    --main-class Calculator `
    --dest $PackageDir

if ($LASTEXITCODE -ne 0) {
    throw "Application image creation failed."
}

Write-Host "=== Creating Windows installer ==="

New-Item -ItemType Directory -Force -Path $InstallerDir | Out-Null

jpackage `
    --type exe `
    --name $AppName `
    --app-version $Version `
    --vendor "theDollar013" `
    --description "JavaFX calculator with basic mathematical functions" `
    --icon $Icon `
    --app-image "$PackageDir\$AppName" `
    --dest $InstallerDir `
    --win-menu `
    --win-shortcut `
    --win-shortcut-prompt `
    --win-dir-chooser `
    --win-with-ui

if ($LASTEXITCODE -ne 0) {
    throw "Installer creation failed."
}

Write-Host ""
Write-Host "=== BUILD COMPLETE ==="
Write-Host "Installer is located in:"
Write-Host "$InstallerDir"

if ($Publish) {

    Write-Host "=== Publishing GitHub Release ==="

    $Installer = Get-ChildItem "$InstallerDir\*.exe" | Select-Object -First 1

    if (-not $Installer) {

        throw "Installer could not be found."
    }

    gh release create $Tag `
        $Installer.FullName `
        --repo $Repo `
        --title "Calculator $Version" `
        --generate-notes

    if ($LASTEXITCODE -ne 0) {

        throw "GitHub release creation failed."
    }

    Write-Host "GitHub release published succesfully."
}