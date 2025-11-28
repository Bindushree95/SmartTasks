# API Test Script for SmartTasks
# This script demonstrates how to test the API endpoints using curl or PowerShell

Write-Host "SmartTasks API Test Script" -ForegroundColor Green
Write-Host "===========================" -ForegroundColor Green
Write-Host ""

$baseUrl = "http://localhost:8080/api"
$token = ""

# Function to make API calls
function Invoke-ApiTest {
    param(
        [string]$Method,
        [string]$Endpoint,
        [object]$Body = $null,
        [string]$Token = ""
    )
    
    $headers = @{
        "Content-Type" = "application/json"
    }
    
    if ($Token) {
        $headers["Authorization"] = "Bearer $Token"
    }
    
    try {
        $params = @{
            Method = $Method
            Uri = "$baseUrl$Endpoint"
            Headers = $headers
        }
        
        if ($Body) {
            $params["Body"] = ($Body | ConvertTo-Json)
        }
        
        $response = Invoke-RestMethod @params
        return $response
    }
    catch {
        Write-Host "Error: $_" -ForegroundColor Red
        return $null
    }
}

Write-Host "1. Testing Registration..." -ForegroundColor Cyan
$registerData = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
}

$authResponse = Invoke-ApiTest -Method POST -Endpoint "/auth/register" -Body $registerData

if ($authResponse) {
    Write-Host "✓ Registration successful!" -ForegroundColor Green
    Write-Host "Token: $($authResponse.token.Substring(0, 20))..." -ForegroundColor Yellow
    $token = $authResponse.token
} else {
    Write-Host "✗ Registration failed" -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "2. Testing Login..." -ForegroundColor Cyan
$loginData = @{
    email = "test@example.com"
    password = "password123"
}

$authResponse = Invoke-ApiTest -Method POST -Endpoint "/auth/login" -Body $loginData

if ($authResponse) {
    Write-Host "✓ Login successful!" -ForegroundColor Green
    $token = $authResponse.token
} else {
    Write-Host "✗ Login failed" -ForegroundColor Red
}

Write-Host ""
Write-Host "3. Creating Tasks..." -ForegroundColor Cyan

$tasks = @(
    @{ title = "Buy groceries"; description = "Milk, bread, eggs" },
    @{ title = "Complete project"; description = "Finish SmartTasks API" },
    @{ title = "Exercise"; description = "30 minutes running" }
)

$taskIds = @()
foreach ($taskData in $tasks) {
    $task = Invoke-ApiTest -Method POST -Endpoint "/tasks" -Body $taskData -Token $token
    if ($task) {
        Write-Host "✓ Task created: $($task.title)" -ForegroundColor Green
        $taskIds += $task.id
    }
}

Write-Host ""
Write-Host "4. Getting All Tasks..." -ForegroundColor Cyan
$allTasks = Invoke-ApiTest -Method GET -Endpoint "/tasks" -Token $token
if ($allTasks) {
    Write-Host "✓ Retrieved $($allTasks.Count) tasks" -ForegroundColor Green
}

Write-Host ""
Write-Host "5. Completing a Task..." -ForegroundColor Cyan
if ($taskIds.Count -gt 0) {
    $completedTask = Invoke-ApiTest -Method PATCH -Endpoint "/tasks/$($taskIds[0])/toggle" -Token $token
    if ($completedTask) {
        Write-Host "✓ Task marked as completed: $($completedTask.title)" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "6. Getting Completed Tasks..." -ForegroundColor Cyan
$completedTasks = Invoke-ApiTest -Method GET -Endpoint "/tasks?completed=true" -Token $token
if ($completedTasks) {
    Write-Host "✓ Retrieved $($completedTasks.Count) completed tasks" -ForegroundColor Green
}

Write-Host ""
Write-Host "7. Getting Statistics..." -ForegroundColor Cyan
$stats = Invoke-ApiTest -Method GET -Endpoint "/stats" -Token $token
if ($stats) {
    Write-Host "✓ Statistics retrieved:" -ForegroundColor Green
    Write-Host "  Total Tasks: $($stats.totalTasks)" -ForegroundColor Yellow
    Write-Host "  Completed: $($stats.completedTasks)" -ForegroundColor Yellow
    Write-Host "  Pending: $($stats.pendingTasks)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "8. Updating a Task..." -ForegroundColor Cyan
if ($taskIds.Count -gt 1) {
    $updateData = @{
        title = "Updated Task"
        description = "This task has been updated"
    }
    $updatedTask = Invoke-ApiTest -Method PUT -Endpoint "/tasks/$($taskIds[1])" -Body $updateData -Token $token
    if ($updatedTask) {
        Write-Host "✓ Task updated: $($updatedTask.title)" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "9. Deleting a Task..." -ForegroundColor Cyan
if ($taskIds.Count -gt 2) {
    $deleteResponse = Invoke-ApiTest -Method DELETE -Endpoint "/tasks/$($taskIds[2])" -Token $token
    if ($deleteResponse) {
        Write-Host "✓ Task deleted successfully" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "All tests completed!" -ForegroundColor Green
Write-Host ""
Write-Host "To manually test the API:" -ForegroundColor Yellow
Write-Host "1. Start the application: mvn spring-boot:run" -ForegroundColor White
Write-Host "2. Use this token for authentication: $token" -ForegroundColor White
Write-Host "3. Access H2 Console: http://localhost:8080/h2-console" -ForegroundColor White
