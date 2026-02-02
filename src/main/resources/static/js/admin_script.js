/**
 * 
 */
// ===========================
// Admin Dashboard Script
// ===========================

// Get API URL from environment or use localhost fallback
const API_URL = window.API_URL || 'http://localhost:9090';
let currentApplicationId = null;
let allApplications = [];
let allFaqs = [];
let allQueries = [];
let allAdminUsers = [];
let loanTypeChart = null;
let statusChart = null;

// currentAdmin and adminToken are already declared in index.html
// Use window to access them or get from session if not available
const adminToken = sessionStorage.getItem('adminToken');

// Helper function to get auth headers
function getAuthHeaders() {
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${adminToken}`
    };
}

// ===========================
// Loading Spinner
// ===========================
window.addEventListener('load', () => {
    const loadingSpinner = document.getElementById('loadingSpinner');
    if (loadingSpinner) {
        loadingSpinner.classList.add('hidden');
    }
});

// Hide spinner after a short delay in case load event is slow
setTimeout(() => {
    const loadingSpinner = document.getElementById('loadingSpinner');
    if (loadingSpinner && !loadingSpinner.classList.contains('hidden')) {
        loadingSpinner.classList.add('hidden');
    }
}, 3000);

// ===========================
// Mobile Sidebar Toggle
// ===========================
const mobileMenuToggle = document.getElementById('mobileMenuToggle');
const sidebar = document.querySelector('.sidebar');
const sidebarOverlay = document.getElementById('sidebarOverlay');

function openSidebar() {
    sidebar.classList.add('active');
    sidebarOverlay.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeSidebar() {
    sidebar.classList.remove('active');
    sidebarOverlay.classList.remove('active');
    document.body.style.overflow = '';
}

if (mobileMenuToggle) {
    mobileMenuToggle.addEventListener('click', () => {
        if (sidebar.classList.contains('active')) {
            closeSidebar();
        } else {
            openSidebar();
        }
    });
}

if (sidebarOverlay) {
    sidebarOverlay.addEventListener('click', closeSidebar);
}

// Close sidebar when nav item is clicked (mobile)
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
        if (window.innerWidth <= 768) {
            closeSidebar();
        }
    });
});

// ===========================
// Page Navigation with URL Hash
// ===========================
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        const pageName = item.getAttribute('data-page');
        window.location.hash = `#${pageName}`;
    });
});

// Handle URL hash changes
window.addEventListener('hashchange', () => {
    const page = window.location.hash.substring(1) || 'dashboard';
    navigateToPage(page);
});

function navigateToPage(pageName) {
    // Hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });

    // Show selected page
    const page = document.getElementById(`${pageName}-page`);
    if (page) {
        page.classList.add('active');
    }

    // Update nav active state
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    const navItem = document.querySelector(`[data-page="${pageName}"]`);
    if (navItem) {
        navItem.classList.add('active');
    }

    // Update page title
    const titles = {
        dashboard: 'Dashboard',
        applications: 'Loan Applications',
        careers: 'Career Applications',
        queries: 'Customer Queries',
        faq: 'FAQ Management',
        users: 'User Management',
        settings: 'Settings'
    };
    document.getElementById('pageTitle').textContent = titles[pageName] || 'Dashboard';

    // Load page data
    if (pageName === 'dashboard') {
        loadDashboardData();
    } else if (pageName === 'applications') {
        loadApplications();
    } else if (pageName === 'careers') {
        loadCareerApplications();
    } else if (pageName === 'queries') {
        loadQueries();
    } else if (pageName === 'faq') {
        loadFaqs();
    } else if (pageName === 'users') {
        loadAdminUsers();
    }
}

// ===========================
// Dashboard
// ===========================
async function loadDashboardData() {
    try {
        const response = await fetch(`${API_URL}/api/applications`);
        const data = await response.json();

        if (data.success) {
            allApplications = data.data || [];
            updateDashboardStats();
            updateCharts();
            loadRecentApplications();
        }
		loadDashboardCounts();
    } catch (error) {
        console.error('Error loading dashboard data:', error);
    }
}

async function loadDashboardData() {
    try {
        const response = await fetch(`${API_URL}/lead/api/v1/count`);
        const data = await response.json();

        console.log("Dashboard API:", data);

        if (data.statusCode == 200) {
            const leadData = data.response;

            document.getElementById("totalApplications").innerText = leadData.totalLeads;
            document.getElementById("approvedCount").innerText = leadData.approvedLeads;
            document.getElementById("rejectedCount").innerText = leadData.rejectedLeads;
            document.getElementById("underReviewCount").innerText = leadData.underReviewLeads;
        }

    } catch (error) {
        console.error("Error loading dashboard data:", error);
    }
}


// Chart instances
let weeklyTrendChart = null;

function updateCharts() {
    // Update center info for doughnut
    const totalApps = allApplications.length;
    const totalCenterEl = document.getElementById('totalAppsCenter');
    if (totalCenterEl) totalCenterEl.textContent = totalApps;
    
    // Calculate rates for metrics
    const approved = allApplications.filter(app => app.applicationStatus === 'approved').length;
    const underReview = allApplications.filter(app => app.applicationStatus === 'under-review' || app.applicationStatus === 'submitted').length;
    const rejected = allApplications.filter(app => app.applicationStatus === 'rejected').length;
    
    const approvalRate = totalApps > 0 ? Math.round((approved / totalApps) * 100) : 0;
    const processingRate = totalApps > 0 ? Math.round((underReview / totalApps) * 100) : 0;
    const rejectionRate = totalApps > 0 ? Math.round((rejected / totalApps) * 100) : 0;
    
    // Update metric circles
    updateMetricCircle('approvalCircle', 'approvalRateValue', approvalRate);
    updateMetricCircle('processingCircle', 'processingRateValue', processingRate);
    updateMetricCircle('rejectionCircle', 'rejectionRateValue', rejectionRate);
    
    // Loan Type Chart - Advanced Bar Chart with Gradient
    const loanTypeCounts = {};
    const loanTypeLabels = {
        'personal-loan': 'Personal',
        'business-loan': 'Business',
        'instant-loan': 'Instant',
        'car-loan': 'Car',
        'credit-card': 'Credit Card',
        'emi-card': 'EMI Card',
        'insurance': 'Insurance',
        'bank-account': 'Bank A/C'
    };
    
    allApplications.forEach(app => {
        const label = loanTypeLabels[app.loanType] || app.loanType;
        loanTypeCounts[label] = (loanTypeCounts[label] || 0) + 1;
    });

    const loanTypeCtx = document.getElementById('loanTypeChart');
    if (loanTypeChart) {
        loanTypeChart.destroy();
    }
    
    // Create gradient for bars
    const barCtx = loanTypeCtx.getContext('2d');
    const barGradient = barCtx.createLinearGradient(0, 0, 0, 300);
    barGradient.addColorStop(0, 'rgba(99, 102, 241, 0.9)');
    barGradient.addColorStop(1, 'rgba(139, 92, 246, 0.9)');
    
    const barHoverGradient = barCtx.createLinearGradient(0, 0, 0, 300);
    barHoverGradient.addColorStop(0, 'rgba(99, 102, 241, 1)');
    barHoverGradient.addColorStop(1, 'rgba(168, 85, 247, 1)');
    
    loanTypeChart = new Chart(loanTypeCtx, {
        type: 'bar',
        data: {
            labels: Object.keys(loanTypeCounts),
            datasets: [{
                label: 'Applications',
                data: Object.values(loanTypeCounts),
                backgroundColor: [
                    'rgba(99, 102, 241, 0.85)',
                    'rgba(139, 92, 246, 0.85)',
                    'rgba(236, 72, 153, 0.85)',
                    'rgba(14, 165, 233, 0.85)',
                    'rgba(16, 185, 129, 0.85)',
                    'rgba(245, 158, 11, 0.85)',
                    'rgba(239, 68, 68, 0.85)',
                    'rgba(99, 102, 241, 0.85)'
                ],
                borderRadius: 10,
                borderSkipped: false,
                hoverBackgroundColor: [
                    'rgba(99, 102, 241, 1)',
                    'rgba(139, 92, 246, 1)',
                    'rgba(236, 72, 153, 1)',
                    'rgba(14, 165, 233, 1)',
                    'rgba(16, 185, 129, 1)',
                    'rgba(245, 158, 11, 1)',
                    'rgba(239, 68, 68, 1)',
                    'rgba(99, 102, 241, 1)'
                ],
                barThickness: 35,
                maxBarThickness: 45
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            animation: {
                duration: 1500,
                easing: 'easeOutQuart'
            },
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(15, 23, 42, 0.9)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    padding: 14,
                    cornerRadius: 10,
                    displayColors: true,
                    boxPadding: 6,
                    callbacks: {
                        label: function(context) {
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = ((context.raw / total) * 100).toFixed(1);
                            return `${context.raw} applications (${percentage}%)`;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(226, 232, 240, 0.5)',
                        drawBorder: false
                    },
                    ticks: {
                        color: '#64748b',
                        font: { size: 11, weight: '500' },
                        padding: 10
                    }
                },
                x: {
                    grid: { display: false },
                    ticks: {
                        color: '#64748b',
                        font: { size: 11, weight: '500' },
                        padding: 8
                    }
                }
            }
        }
    });

    // Status Chart - Advanced Doughnut
    const statusCounts = {
        'submitted': 0,
        'under-review': 0,
        'approved': 0,
        'rejected': 0,
        'on-hold': 0
    };
    allApplications.forEach(app => {
        statusCounts[app.applicationStatus]++;
    });

    const statusCtx = document.getElementById('statusChart');
    if (statusChart) {
        statusChart.destroy();
    }
    
    statusChart = new Chart(statusCtx, {
        type: 'doughnut',
        data: {
            labels: ['Submitted', 'Under Review', 'Approved', 'Rejected', 'On Hold'],
            datasets: [{
                data: Object.values(statusCounts),
                backgroundColor: [
                    'rgba(59, 130, 246, 0.85)',
                    'rgba(245, 158, 11, 0.85)',
                    'rgba(16, 185, 129, 0.85)',
                    'rgba(239, 68, 68, 0.85)',
                    'rgba(139, 92, 246, 0.85)'
                ],
                hoverBackgroundColor: [
                    'rgba(59, 130, 246, 1)',
                    'rgba(245, 158, 11, 1)',
                    'rgba(16, 185, 129, 1)',
                    'rgba(239, 68, 68, 1)',
                    'rgba(139, 92, 246, 1)'
                ],
                borderColor: 'rgba(255, 255, 255, 0.9)',
                borderWidth: 3,
                hoverBorderWidth: 4,
                hoverOffset: 15
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            cutout: '65%',
            animation: {
                animateRotate: true,
                animateScale: true,
                duration: 1500,
                easing: 'easeOutQuart'
            },
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        usePointStyle: true,
                        pointStyle: 'circle',
                        font: { size: 12, weight: '500' },
                        color: '#475569'
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(15, 23, 42, 0.9)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    padding: 14,
                    cornerRadius: 10,
                    displayColors: true,
                    boxPadding: 6,
                    callbacks: {
                        label: function(context) {
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = total > 0 ? ((context.raw / total) * 100).toFixed(1) : 0;
                            return `${context.label}: ${context.raw} (${percentage}%)`;
                        }
                    }
                }
            }
        }
    });
    
    // Weekly Trend Chart - Line Chart
    updateWeeklyTrendChart();
}

function updateMetricCircle(circleId, valueId, percentage) {
    const circle = document.getElementById(circleId);
    const valueEl = document.getElementById(valueId);
    
    if (circle && valueEl) {
        const circumference = 2 * Math.PI * 45; // r = 45
        const offset = circumference - (percentage / 100) * circumference;
        
        setTimeout(() => {
            circle.style.strokeDashoffset = offset;
        }, 300);
        
        valueEl.textContent = percentage + '%';
    }
}

function updateWeeklyTrendChart() {
    const weeklyTrendCtx = document.getElementById('weeklyTrendChart');
    if (!weeklyTrendCtx) return;
    
    // Get last 7 days data
    const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const today = new Date();
    const weekData = [];
    const weekLabels = [];
    
    for (let i = 6; i >= 0; i--) {
        const date = new Date(today);
        date.setDate(date.getDate() - i);
        weekLabels.push(days[date.getDay()]);
        
        // Count applications for this date
        const count = allApplications.filter(app => {
            const appDate = new Date(app.applicationDate);
            return appDate.toDateString() === date.toDateString();
        }).length;
        weekData.push(count);
    }
    
    if (weeklyTrendChart) {
        weeklyTrendChart.destroy();
    }
    
    const ctx = weeklyTrendCtx.getContext('2d');
    const gradient = ctx.createLinearGradient(0, 0, 0, 250);
    gradient.addColorStop(0, 'rgba(99, 102, 241, 0.4)');
    gradient.addColorStop(1, 'rgba(99, 102, 241, 0.02)');
    
    weeklyTrendChart = new Chart(weeklyTrendCtx, {
        type: 'line',
        data: {
            labels: weekLabels,
            datasets: [{
                label: 'Applications',
                data: weekData,
                fill: true,
                backgroundColor: gradient,
                borderColor: 'rgba(99, 102, 241, 1)',
                borderWidth: 3,
                tension: 0.4,
                pointBackgroundColor: 'white',
                pointBorderColor: 'rgba(99, 102, 241, 1)',
                pointBorderWidth: 3,
                pointRadius: 5,
                pointHoverRadius: 8,
                pointHoverBackgroundColor: 'rgba(99, 102, 241, 1)',
                pointHoverBorderColor: 'white',
                pointHoverBorderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            animation: {
                duration: 1500,
                easing: 'easeOutQuart'
            },
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(15, 23, 42, 0.9)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    padding: 14,
                    cornerRadius: 10,
                    displayColors: false,
                    callbacks: {
                        label: function(context) {
                            return `${context.raw} applications`;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(226, 232, 240, 0.5)',
                        drawBorder: false
                    },
                    ticks: {
                        color: '#64748b',
                        font: { size: 11, weight: '500' },
                        padding: 10,
                        stepSize: 1
                    }
                },
                x: {
                    grid: { display: false },
                    ticks: {
                        color: '#64748b',
                        font: { size: 11, weight: '500' },
                        padding: 8
                    }
                }
            },
            interaction: {
                intersect: false,
                mode: 'index'
            }
        }
    });
}

function loadRecentApplications() {
    const tbody = document.getElementById('recentApplicationsTable');
    const recent = allApplications.slice(0, 5);

    tbody.innerHTML = recent.map(app => {
        // Determine what to show in the "Amount" column based on form type
        let amountDisplay = 'N/A';
        if (app.loanType === 'insurance' || app.loanType === 'bank-account') {
            amountDisplay = app.monthlyIncome ? `₹${app.monthlyIncome.toLocaleString()}` : 'N/A';
        } else if (app.loanType === 'emi-card') {
            amountDisplay = app.employmentType || 'N/A';
        } else if (app.loanAmount) {
            amountDisplay = `₹${app.loanAmount.toLocaleString()}`;
        }
        
        return `
        <tr>
            <td>${app.fullName}</td>
            <td>${formatLoanType(app.loanType)}</td>
            <td>${amountDisplay}</td>
            <td><span class="status-badge status-${app.applicationStatus}">${formatStatus(app.applicationStatus)}</span></td>
            <td>${formatDate(app.applicationDate)}</td>
            <td><button class="btn-primary btn-small" onclick="viewApplicationDetails('${app._id}')">View</button></td>
        </tr>
    `}).join('');
}

// ===========================
// Loan Applications
// ===========================
async function loadApplications() {
    try {
        const response = await fetch(`${API_URL}/api/applications`);
        const data = await response.json();

        if (data.success) {
            allApplications = data.data || [];
            displayApplications(allApplications);
        }
    } catch (error) {
        console.error('Error loading applications:', error);
    }
}

function displayApplications(applications) {
    const tbody = document.getElementById('applicationsTable');

    if (applications.length === 0) {
        tbody.innerHTML = '<tr><td colspan="9" style="text-align: center; padding: 20px;">No applications found</td></tr>';
        return;
    }

    tbody.innerHTML = applications.map(app => {
        // Determine what to show in the "Amount" column based on form type
        let amountDisplay = 'N/A';
        if (app.loanType === 'insurance' || app.loanType === 'bank-account') {
            amountDisplay = app.monthlyIncome ? `₹${app.monthlyIncome.toLocaleString()} (Income)` : 'N/A';
        } else if (app.loanType === 'emi-card') {
            amountDisplay = app.employmentType || 'N/A';
        } else if (app.loanAmount) {
            amountDisplay = `₹${app.loanAmount.toLocaleString()}`;
        }
        
        return `
        <tr>
            <td>${app._id.substring(0, 8)}...</td>
            <td>${app.fullName}</td>
            <td>${app.personalEmail}</td>
            <td>${app.mobileNumber}</td>
            <td>${formatLoanType(app.loanType)}</td>
            <td>${amountDisplay}</td>
            <td><span class="status-badge status-${app.applicationStatus}">${formatStatus(app.applicationStatus)}</span></td>
            <td>${formatDate(app.applicationDate)}</td>
            <td>
                <div class="action-btns" style="display: flex; gap: 5px; align-items: center;">
                    <button class="btn-primary btn-small" onclick="viewApplicationDetails('${app._id}')" title="View Details">View</button>
                    <button class="btn-secondary btn-small" onclick="editApplication('${app._id}')" title="Edit Application"><i class="fas fa-edit"></i></button>
                    <a href="tel:${app.mobileNumber}" class="btn-icon btn-success" title="Call ${app.fullName}" style="padding: 6px 10px; color: white; text-decoration: none; border-radius: 5px;"><i class="fas fa-phone"></i></a>
                    <a href="https://wa.me/91${app.mobileNumber}" target="_blank" class="btn-icon btn-whatsapp" title="WhatsApp ${app.fullName}" style="padding: 6px 10px; color: white; background: #25D366; text-decoration: none; border-radius: 5px;"><i class="fab fa-whatsapp"></i></a>
                </div>
            </td>
        </tr>
    `}).join('');
}

async function viewApplicationDetails(appId) {
    try {
        const response = await fetch(`${API_URL}/api/applications/${appId}`);
        const data = await response.json();

        if (data.success) {
            const app = data.data;
            currentApplicationId = appId;

            const modalBody = document.getElementById('appModalBody');
            
            // Determine form type
            const isSimpleForm = ['insurance', 'bank-account', 'emi-card'].includes(app.loanType);
            
            let detailsHTML = `<div class="application-details">`;
            
            // Personal Information (always shown)
            detailsHTML += `
                <h4>Personal Information</h4>
                <p><strong>Name:</strong> ${app.fullName || 'N/A'}</p>
                <p><strong>Email:</strong> ${app.personalEmail || 'N/A'}</p>
                <p><strong>Phone:</strong> ${app.mobileNumber || 'N/A'}</p>
                <p><strong>PAN Card:</strong> ${app.panCardNumber || 'N/A'}</p>
            `;
            
            // Application Type Information
            detailsHTML += `<h4 style="margin-top: 20px;">Application Information</h4>`;
            detailsHTML += `<p><strong>Type:</strong> ${formatLoanType(app.loanType)}</p>`;
            detailsHTML += `<p><strong>Status:</strong> ${formatStatus(app.applicationStatus)}</p>`;
            
            // Insurance specific
            if (app.loanType === 'insurance') {
                detailsHTML += `<p><strong>Insurance Type:</strong> ${formatLoanType(app.insuranceType) || 'N/A'}</p>`;
                detailsHTML += `<p><strong>Monthly Income:</strong> ₹${app.monthlyIncome ? app.monthlyIncome.toLocaleString() : 'N/A'}</p>`;
            }
            // Bank Account specific
            else if (app.loanType === 'bank-account') {
                detailsHTML += `<p><strong>Account Type:</strong> ${formatLoanType(app.accountType) || 'N/A'}</p>`;
                detailsHTML += `<p><strong>Monthly Income:</strong> ₹${app.monthlyIncome ? app.monthlyIncome.toLocaleString() : 'N/A'}</p>`;
            }
            // EMI Card specific
            else if (app.loanType === 'emi-card') {
                detailsHTML += `<p><strong>Employment Type:</strong> ${app.employmentType || 'N/A'}</p>`;
            }
            // Full Loan Form
            else {
                detailsHTML += `<p><strong>Loan Amount:</strong> ₹${app.loanAmount ? app.loanAmount.toLocaleString() : 'N/A'}</p>`;
                detailsHTML += `<p><strong>Employment Type:</strong> ${app.employmentType || 'N/A'}</p>`;
                
                if (app.maritalStatus) {
                    detailsHTML += `
                        <h4 style="margin-top: 20px;">Additional Personal Info</h4>
                        <p><strong>Marital Status:</strong> ${app.maritalStatus || 'N/A'}</p>
                        <p><strong>Mother Name:</strong> ${app.motherName || 'N/A'}</p>
                    `;
                }
                
                if (app.currentAddress && app.currentAddress.address) {
                    detailsHTML += `
                        <h4 style="margin-top: 20px;">Address Information</h4>
                        <p><strong>Current Address:</strong> ${app.currentAddress.address || ''}, ${app.currentAddress.city || ''}, ${app.currentAddress.zipcode || ''}</p>
                        <p><strong>Permanent Address:</strong> ${app.permanentAddress ? (app.permanentAddress.address || '') + ', ' + (app.permanentAddress.city || '') + ', ' + (app.permanentAddress.zipcode || '') : 'N/A'}</p>
                    `;
                }

                // Show Business Information for business-loan, Employment Information for others
                if (app.companyName) {
                    if (app.loanType === 'business-loan') {
                        detailsHTML += `
                            <h4 style="margin-top: 20px;">Business Information</h4>
                            <p><strong>Business Name:</strong> ${app.companyName || 'N/A'}</p>
                            <p><strong>Business Address:</strong> ${app.companyAddress ? (app.companyAddress.address || '') + ', ' + (app.companyAddress.city || '') + ', ' + (app.companyAddress.zipcode || '') : 'N/A'}</p>
                            <p><strong>Role/Position:</strong> ${app.designation || 'N/A'}</p>
                            <p><strong>Business Vintage:</strong> ${app.businessVintage ? app.businessVintage + ' Years' : 'N/A'}</p>
                        `;
                    } else {
                        detailsHTML += `
                            <h4 style="margin-top: 20px;">Employment Information</h4>
                            <p><strong>Company:</strong> ${app.companyName || 'N/A'}</p>
                            <p><strong>Company Address:</strong> ${app.companyAddress ? (app.companyAddress.address || '') + ', ' + (app.companyAddress.city || '') + ', ' + (app.companyAddress.zipcode || '') : 'N/A'}</p>
                            <p><strong>Designation:</strong> ${app.designation || 'N/A'}</p>
                            <p><strong>Official Email:</strong> ${app.officialEmail || 'N/A'}</p>
                            <p><strong>Current Experience:</strong> ${app.currentWorkExperience || 0} months</p>
                            <p><strong>Total Experience:</strong> ${app.totalWorkExperience || 0} months</p>
                        `;
                    }
                }
                
                // Business loan fields
                if (app.loanType === 'business-loan' && app.monthlyIncome) {
                    detailsHTML += `
                        <h4 style="margin-top: 20px;">Business Financial Details</h4>
                        <p><strong>Monthly Income:</strong> ₹${app.monthlyIncome.toLocaleString()}</p>
                        <p><strong>GST Registered:</strong> ${app.gstRegistered === 'yes' ? 'Yes' : app.gstRegistered === 'no' ? 'No' : 'N/A'}</p>
                        <p><strong>ITR Filed:</strong> ${app.itrReturn === 'yes' ? 'Yes' : app.itrReturn === 'no' ? 'No' : 'N/A'}</p>
                    `;
                }
                
                // Employee salary fields
                if (app.loanType !== 'business-loan' && app.employmentType === 'employed' && app.monthlyInhandSalary) {
                    detailsHTML += `
                        <h4 style="margin-top: 20px;">Salary Details</h4>
                        <p><strong>Monthly In-Hand Salary:</strong> ₹${app.monthlyInhandSalary.toLocaleString()}</p>
                        <p><strong>PF Deduction:</strong> ${app.pfDeduction === 'yes' ? 'Yes' : app.pfDeduction === 'no' ? 'No' : 'N/A'}</p>
                    `;
                }
            }
            
            detailsHTML += `
                <h4 style="margin-top: 20px;">Application Date</h4>
                <p>${formatDate(app.applicationDate)}</p>
            </div>`;
            
            modalBody.innerHTML = detailsHTML;

            const actionBtns = document.getElementById('appActionBtns');
            actionBtns.innerHTML = `
                <div style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
                    <a href="tel:${app.mobileNumber}" class="btn-success" title="Call ${app.fullName}" style="display: inline-flex; align-items: center; gap: 5px;">
                        <i class="fas fa-phone"></i> Call
                    </a>
                    <a href="https://wa.me/91${app.mobileNumber}" target="_blank" class="btn-whatsapp" title="WhatsApp ${app.fullName}" style="display: inline-flex; align-items: center; gap: 5px; background: #25D366; color: white; padding: 10px 16px; border-radius: 6px; text-decoration: none; font-weight: 500;">
                        <i class="fab fa-whatsapp"></i> WhatsApp
                    </a>
                    <select id="statusSelect" style="padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
                        <option value="submitted" ${app.applicationStatus === 'submitted' ? 'selected' : ''}>Submitted</option>
                        <option value="under-review" ${app.applicationStatus === 'under-review' ? 'selected' : ''}>Under Review</option>
                        <option value="approved" ${app.applicationStatus === 'approved' ? 'selected' : ''}>Approved</option>
                        <option value="rejected" ${app.applicationStatus === 'rejected' ? 'selected' : ''}>Rejected</option>
                        <option value="on-hold" ${app.applicationStatus === 'on-hold' ? 'selected' : ''}>On Hold</option>
                    </select>
                    <button class="btn-primary" onclick="updateApplicationStatus('${appId}')">Update Status</button>
                    <button class="btn-secondary" onclick="closeAppModal();editApplication('${appId}')"><i class="fas fa-edit"></i> Edit</button>
                </div>
            `;

            document.getElementById('appDetailModal').classList.add('active');
        }
    } catch (error) {
        console.error('Error loading application details:', error);
    }
}

async function updateApplicationStatus(appId) {
    const status = document.getElementById('statusSelect').value;

    try {
        const response = await fetch(`${API_URL}/api/applications/${appId}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status })
        });

        const data = await response.json();
        if (data.success) {
            alert('Application status updated successfully');
            closeAppModal();
            loadApplications();
        }
    } catch (error) {
        console.error('Error updating status:', error);
    }
}

// ===========================
// Customer Queries
// ===========================
async function loadQueries() {
    try {
        const response = await fetch(`${API_URL}/api/contact-messages`);
        const data = await response.json();

        if (data.success) {
            allQueries = data.data || [];
            displayQueries();
        }
    } catch (error) {
        console.error('Error loading queries:', error);
        // Display sample queries if API fails
        displaySampleQueries();
    }
}

function displayQueries() {
    const queriesList = document.getElementById('queriesList');

    if (allQueries.length === 0) {
        queriesList.innerHTML = '<p style="text-align: center; padding: 20px;">No queries found</p>';
        return;
    }

    queriesList.innerHTML = allQueries.map((query, index) => `
        <div class="query-item ${index === 0 ? 'active' : ''}" onclick="selectQuery(${index})">
            <p style="font-weight: 600; margin: 0 0 5px 0;">${query.name}</p>
            <p style="color: #666; margin: 0 0 5px 0; font-size: 0.85rem;">${query.service}</p>
            <p style="color: #999; margin: 0; font-size: 0.8rem;">${formatDate(query.date || new Date())}</p>
        </div>
    `).join('');

    if (allQueries.length > 0) {
        selectQuery(0);
    }
}

function displaySampleQueries() {
    const queries = [
        { name: 'Rajesh Kumar', email: 'rajesh@email.com', phone: '9876543210', service: 'Personal Loan', message: 'I need a personal loan for my wedding. What is the interest rate?', date: new Date() },
        { name: 'Priya Sharma', email: 'priya@email.com', phone: '9876543211', service: 'Business Loan', message: 'How much can I borrow for my startup?', date: new Date(Date.now() - 86400000) },
        { name: 'Amit Patel', email: 'amit@email.com', phone: '9876543212', service: 'Car Loan', message: 'What documents are required for car loan?', date: new Date(Date.now() - 172800000) }
    ];

    allQueries = queries;
    displayQueries();
}

function selectQuery(index) {
    document.querySelectorAll('.query-item').forEach(item => item.classList.remove('active'));
    document.querySelectorAll('.query-item')[index].classList.add('active');

    const query = allQueries[index];
    document.getElementById('qName').textContent = query.name;
    document.getElementById('qEmail').textContent = query.email;
    document.getElementById('qPhone').textContent = query.phone;
    document.getElementById('qService').textContent = query.service;
    document.getElementById('qMessage').textContent = query.message;
    document.getElementById('queryDetails').style.display = 'block';
}

function updateQueryStatus() {
    alert('Query status updated successfully');
}

// ===========================
// FAQ Management
// ===========================
async function loadFaqs() {
    try {
        const response = await fetch(`${API_URL}/api/faqs`);
        const data = await response.json();

        if (data.success) {
            allFaqs = data.data || [];
            displayFaqs();
        }
    } catch (error) {
        console.error('Error loading FAQs:', error);
        loadDefaultFaqs();
    }
}

function loadDefaultFaqs() {
    allFaqs = [
        {
            _id: '1',
            question: 'What is the minimum loan amount?',
            answer: 'The minimum loan amount varies by loan type. For personal loans, the minimum is ₹10,000.',
            category: 'loans'
        },
        {
            _id: '2',
            question: 'How long does approval take?',
            answer: 'Most loans are approved within 24 hours of document submission.',
            category: 'general'
        },
        {
            _id: '3',
            question: 'What documents are required?',
            answer: 'You will need valid ID, address proof, income documents, and bank statements.',
            category: 'general'
        },
        {
            _id: '4',
            question: 'Can I prepay my loan?',
            answer: 'Yes, you can prepay your loan with minimal or no penalty charges.',
            category: 'loans'
        }
    ];
    displayFaqs();
}

function displayFaqs() {
    const faqList = document.getElementById('faqList');

    if (allFaqs.length === 0) {
        faqList.innerHTML = '<p style="text-align: center; padding: 20px;">No FAQs found</p>';
        return;
    }

    faqList.innerHTML = allFaqs.map(faq => `
        <div class="faq-item">
            <div class="faq-item-header">
                <div>
                    <h4>${faq.question}</h4>
                    <p>${faq.answer}</p>
                </div>
                <div class="faq-actions">
                    <button onclick="editFaq('${faq._id}')">Edit</button>
                    <button onclick="deleteFaq('${faq._id}')">Delete</button>
                </div>
            </div>
        </div>
    `).join('');
}

document.getElementById('addFaqBtn').addEventListener('click', () => {
    document.getElementById('faqModalTitle').textContent = 'Add New FAQ';
    document.getElementById('faqForm').reset();
    openFaqModal();
});

function openFaqModal() {
    document.getElementById('faqModal').classList.add('active');
}

function closeFaqModal() {
    document.getElementById('faqModal').classList.remove('active');
}

async function saveFaq() {
    const question = document.getElementById('faqQuestion').value;
    const answer = document.getElementById('faqAnswer').value;
    const category = document.getElementById('faqCategory').value;

    if (!question || !answer) {
        alert('Please fill all required fields');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/api/faqs`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ question, answer, category })
        });

        const data = await response.json();
        if (data.success) {
            loadFaqs();
            closeFaqModal();
            alert('FAQ added successfully');
        }
    } catch (error) {
        console.error('Error saving FAQ:', error);
        alert('Error saving FAQ');
    }
}

async function editFaq(faqId) {
    const faq = allFaqs.find(f => f._id === faqId);
    if (faq) {
        document.getElementById('faqModalTitle').textContent = 'Edit FAQ';
        document.getElementById('faqQuestion').value = faq.question;
        document.getElementById('faqAnswer').value = faq.answer;
        document.getElementById('faqCategory').value = faq.category;
        document.getElementById('faqForm').setAttribute('data-faq-id', faqId);
        openFaqModal();
    }
}

async function deleteFaq(faqId) {
    if (confirm('Are you sure you want to delete this FAQ?')) {
        try {
            const response = await fetch(`${API_URL}/api/faqs/${faqId}`, {
                method: 'DELETE'
            });

            const data = await response.json();
            if (data.success) {
                loadFaqs();
                alert('FAQ deleted successfully');
            }
        } catch (error) {
            console.error('Error deleting FAQ:', error);
            alert('Error deleting FAQ');
        }
    }
}

// ===========================
// Utility Functions
// ===========================
function formatLoanType(type) {
    const types = {
        'personal-loan': 'Personal Loan',
        'business-loan': 'Business Loan',
        'instant-loan': 'Instant Loan',
        'car-loan': 'Car Loan',
        'credit-card': 'Credit Card',
        'emi-card': 'EMI Card',
        'insurance': 'Insurance',
        'bank-account': 'Bank Account Opening'
    };
    return types[type] || type;
}

function formatStatus(status) {
    const statuses = {
        'submitted': 'Submitted',
        'under-review': 'Under Review',
        'approved': 'Approved',
        'rejected': 'Rejected',
        'on-hold': 'On Hold'
    };
    return statuses[status] || status;
}

function formatDate(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' }) + 
           ' at ' + 
           date.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true });
}

function closeAppModal() {
    document.getElementById('appDetailModal').classList.remove('active');
}

// ===========================
// Edit Application
// ===========================
async function editApplication(appId) {
    try {
        const response = await fetch(`${API_URL}/api/applications/${appId}`);
        const data = await response.json();

        if (data.success) {
            const app = data.data;
            currentApplicationId = appId;
            
            const isSimpleForm = ['insurance', 'bank-account', 'emi-card'].includes(app.loanType);
            
            let formHTML = `
                <form id="editApplicationForm" style="max-height: 60vh; overflow-y: auto;">
                    <h4><i class="fas fa-edit"></i> Edit Application Details</h4>
                    
                    <!-- Personal Information -->
                    <div class="form-section">
                        <h5>Personal Information</h5>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Full Name *</label>
                                <input type="text" id="edit_fullName" value="${app.fullName || ''}" required>
                            </div>
                            <div class="form-group">
                                <label>Mobile Number *</label>
                                <input type="tel" id="edit_mobileNumber" value="${app.mobileNumber || ''}" required>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Email *</label>
                                <input type="email" id="edit_personalEmail" value="${app.personalEmail || ''}" required>
                            </div>
                            <div class="form-group">
                                <label>PAN Card Number *</label>
                                <input type="text" id="edit_panCardNumber" value="${app.panCardNumber || ''}" required>
                            </div>
                        </div>
                    </div>
            `;
            
            if (isSimpleForm) {
                // Simple Forms
                if (app.loanType === 'insurance') {
                    formHTML += `
                        <div class="form-section">
                            <h5>Insurance Details</h5>
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Insurance Type</label>
                                    <select id="edit_insuranceType">
                                        <option value="life-insurance" ${app.insuranceType === 'life-insurance' ? 'selected' : ''}>Life Insurance</option>
                                        <option value="health-insurance" ${app.insuranceType === 'health-insurance' ? 'selected' : ''}>Health Insurance</option>
                                        <option value="vehicle-insurance" ${app.insuranceType === 'vehicle-insurance' ? 'selected' : ''}>Vehicle Insurance</option>
                                        <option value="home-insurance" ${app.insuranceType === 'home-insurance' ? 'selected' : ''}>Home Insurance</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Monthly Income</label>
                                    <input type="number" id="edit_monthlyIncome" value="${app.monthlyIncome || ''}">
                                </div>
                            </div>
                        </div>
                    `;
                } else if (app.loanType === 'bank-account') {
                    formHTML += `
                        <div class="form-section">
                            <h5>Bank Account Details</h5>
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Account Type</label>
                                    <select id="edit_accountType">
                                        <option value="savings" ${app.accountType === 'savings' ? 'selected' : ''}>Savings Account</option>
                                        <option value="current" ${app.accountType === 'current' ? 'selected' : ''}>Current Account</option>
                                        <option value="salary" ${app.accountType === 'salary' ? 'selected' : ''}>Salary Account</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Monthly Income</label>
                                    <input type="number" id="edit_monthlyIncome" value="${app.monthlyIncome || ''}">
                                </div>
                            </div>
                        </div>
                    `;
                } else if (app.loanType === 'emi-card') {
                    formHTML += `
                        <div class="form-section">
                            <h5>EMI Card Details</h5>
                            <div class="form-group">
                                <label>Employment Type</label>
                                <select id="edit_employmentType">
                                    <option value="employed" ${app.employmentType === 'employed' ? 'selected' : ''}>Employed</option>
                                    <option value="self-employed" ${app.employmentType === 'self-employed' ? 'selected' : ''}>Self Employed</option>
                                </select>
                            </div>
                        </div>
                    `;
                }
            } else {
                // Full Loan Form
                formHTML += `
                    <div class="form-section">
                        <h5>Loan Details</h5>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Loan Amount *</label>
                                <input type="number" id="edit_loanAmount" value="${app.loanAmount || ''}" required>
                            </div>
                            <div class="form-group">
                                <label>Employment Type</label>
                                <select id="edit_employmentType">
                                    <option value="employed" ${app.employmentType === 'employed' ? 'selected' : ''}>Employed</option>
                                    <option value="self-employed" ${app.employmentType === 'self-employed' ? 'selected' : ''}>Self Employed</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Marital Status</label>
                                <select id="edit_maritalStatus">
                                    <option value="married" ${app.maritalStatus === 'married' ? 'selected' : ''}>Married</option>
                                    <option value="unmarried" ${app.maritalStatus === 'unmarried' ? 'selected' : ''}>Unmarried</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Mother Name</label>
                                <input type="text" id="edit_motherName" value="${app.motherName || ''}">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-section">
                        <h5>Address Information</h5>
                        <div class="form-group">
                            <label>Current Address</label>
                            <input type="text" id="edit_currentAddress" value="${app.currentAddress?.address || ''}">
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label>City</label>
                                <input type="text" id="edit_currentCity" value="${app.currentAddress?.city || ''}">
                            </div>
                            <div class="form-group">
                                <label>Zipcode</label>
                                <input type="text" id="edit_currentZipcode" value="${app.currentAddress?.zipcode || ''}">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-section">
                        <h5>Company Information</h5>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Company Name</label>
                                <input type="text" id="edit_companyName" value="${app.companyName || ''}">
                            </div>
                            <div class="form-group">
                                <label>Designation</label>
                                <input type="text" id="edit_designation" value="${app.designation || ''}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Official Email</label>
                            <input type="email" id="edit_officialEmail" value="${app.officialEmail || ''}">
                        </div>
                    </div>
                `;
            }
            
            formHTML += `</form>`;
            
            document.getElementById('appModalBody').innerHTML = formHTML;
            document.getElementById('appActionBtns').innerHTML = `
                <button class="btn-secondary" onclick="closeAppModal()">Cancel</button>
                <button class="btn-primary" onclick="saveEditedApplication()"><i class="fas fa-save"></i> Save Changes</button>
            `;
            
            document.getElementById('appDetailModal').classList.add('active');
        }
    } catch (error) {
        console.error('Error loading application for edit:', error);
        alert('Error loading application details');
    }
}

async function saveEditedApplication() {
    try {
        const isSimpleForm = document.getElementById('edit_loanAmount') === null;
        
        let updateData = {
            fullName: document.getElementById('edit_fullName').value,
            mobileNumber: document.getElementById('edit_mobileNumber').value,
            personalEmail: document.getElementById('edit_personalEmail').value,
            panCardNumber: document.getElementById('edit_panCardNumber').value.toUpperCase()
        };
        
        if (isSimpleForm) {
            const monthlyIncomeEl = document.getElementById('edit_monthlyIncome');
            if (monthlyIncomeEl) updateData.monthlyIncome = parseFloat(monthlyIncomeEl.value) || null;
            
            const insuranceTypeEl = document.getElementById('edit_insuranceType');
            if (insuranceTypeEl) updateData.insuranceType = insuranceTypeEl.value;
            
            const accountTypeEl = document.getElementById('edit_accountType');
            if (accountTypeEl) updateData.accountType = accountTypeEl.value;
            
            const employmentTypeEl = document.getElementById('edit_employmentType');
            if (employmentTypeEl) updateData.employmentType = employmentTypeEl.value;
        } else {
            updateData.loanAmount = parseFloat(document.getElementById('edit_loanAmount').value);
            updateData.employmentType = document.getElementById('edit_employmentType').value;
            updateData.maritalStatus = document.getElementById('edit_maritalStatus').value;
            updateData.motherName = document.getElementById('edit_motherName').value;
            updateData.currentAddress = {
                address: document.getElementById('edit_currentAddress').value,
                city: document.getElementById('edit_currentCity').value,
                zipcode: document.getElementById('edit_currentZipcode').value
            };
            updateData.companyName = document.getElementById('edit_companyName').value;
            updateData.designation = document.getElementById('edit_designation').value;
            updateData.officialEmail = document.getElementById('edit_officialEmail').value;
        }
        
        const response = await fetch(`${API_URL}/api/applications/${currentApplicationId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updateData)
        });
        
        const data = await response.json();
        if (data.success) {
            alert('Application updated successfully!');
            closeAppModal();
            loadApplications();
            if (window.location.hash === '#dashboard') {
                loadDashboardData();
            }
        } else {
            alert(data.message || 'Error updating application');
        }
    } catch (error) {
        console.error('Error saving application:', error);
        alert('Error saving application');
    }
}

// ===========================
// Filter & Search Applications
// ===========================
function filterAndSearchApplications() {
    const searchTerm = document.getElementById('applicationSearch').value.toLowerCase().trim();
    const status = document.getElementById('statusFilter').value;
    const loanType = document.getElementById('loanTypeFilter').value;

    let filtered = allApplications;

    // Search filter
    if (searchTerm) {
        filtered = filtered.filter(app => {
            const name = (app.fullName || '').toLowerCase();
            const email = (app.email || '').toLowerCase();
            const phone = (app.phone || '').toLowerCase();
            const id = (app._id || '').toLowerCase();
            
            return name.includes(searchTerm) || 
                   email.includes(searchTerm) || 
                   phone.includes(searchTerm) ||
                   id.includes(searchTerm);
        });
    }

    // Status filter
    if (status) {
        filtered = filtered.filter(app => app.applicationStatus === status);
    }

    // Loan type filter
    if (loanType) {
        filtered = filtered.filter(app => app.loanType === loanType);
    }

    displayApplications(filtered);
}

// Apply filters button
document.getElementById('applyFiltersBtn').addEventListener('click', filterAndSearchApplications);

// Clear filters button
document.getElementById('clearFiltersBtn').addEventListener('click', () => {
    document.getElementById('applicationSearch').value = '';
    document.getElementById('statusFilter').value = '';
    document.getElementById('loanTypeFilter').value = '';
    displayApplications(allApplications);
});

// Search on typing (with debounce)
let searchTimeout;
document.getElementById('applicationSearch').addEventListener('input', () => {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(filterAndSearchApplications, 300);
});

// Search on Enter key
document.getElementById('applicationSearch').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        clearTimeout(searchTimeout);
        filterAndSearchApplications();
    }
});

// ===========================
// Career Applications Management
// ===========================
let allCareerApplications = [];
let currentCareerAppId = null;

async function loadCareerApplications() {
    try {
        // Load stats
        const statsResponse = await fetch(`${API_URL}/api/career/stats`);
        const statsData = await statsResponse.json();
        
        if (statsData.success) {
            document.getElementById('totalCareerApps').textContent = statsData.stats.total;
            document.getElementById('shortlistedCount').textContent = statsData.stats.shortlisted;
            document.getElementById('interviewCount').textContent = statsData.stats.interviewScheduled;
            document.getElementById('selectedCount').textContent = statsData.stats.selected;
        }

        // Load applications
        const response = await fetch(`${API_URL}/api/career/applications`);
        const data = await response.json();

        if (data.success) {
            allCareerApplications = data.applications;
            displayCareerApplications(data.applications);
        }
    } catch (error) {
        console.error('Error loading career applications:', error);
        document.getElementById('careerApplicationsTable').innerHTML = `
            <tr>
                <td colspan="9" style="text-align: center; padding: 20px; color: #dc3545;">
                    Error loading applications. Please try again.
                </td>
            </tr>
        `;
    }
}

function displayCareerApplications(applications) {
    const tableBody = document.getElementById('careerApplicationsTable');
    
    if (applications.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="9" style="text-align: center; padding: 20px;">No career applications found</td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = applications.map(app => `
        <tr>
            <td>${app.fullName}</td>
            <td>${app.email}</td>
            <td>${app.phone}</td>
            <td>${app.position}</td>
            <td>${app.experience}</td>
            <td>${app.location}</td>
            <td><span class="status-badge status-${app.status}">${formatCareerStatus(app.status)}</span></td>
            <td>${new Date(app.appliedAt).toLocaleDateString()}</td>
            <td>
                <div class="action-btns">
                    <button class="btn-icon btn-view" onclick="viewCareerApplication('${app._id}')" title="View Details">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn-icon btn-delete" onclick="deleteCareerApplication('${app._id}')" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function formatCareerStatus(status) {
    const statusMap = {
        'new': 'New',
        'reviewed': 'Reviewed',
        'shortlisted': 'Shortlisted',
        'interview-scheduled': 'Interview',
        'selected': 'Selected',
        'rejected': 'Rejected'
    };
    return statusMap[status] || status;
}

async function viewCareerApplication(id) {
    try {
        currentCareerAppId = id;
        const response = await fetch(`${API_URL}/api/career/applications/${id}`);
        const data = await response.json();

        if (data.success) {
            const app = data.application;
            
            document.getElementById('careerModalBody').innerHTML = `
                <div class="app-details-grid">
                    <div class="detail-section">
                        <h4><i class="fas fa-user"></i> Personal Information</h4>
                        <div class="detail-row">
                            <span class="label">Full Name:</span>
                            <span class="value">${app.fullName}</span>
                        </div>
                        <div class="detail-row">
                            <span class="label">Email:</span>
                            <span class="value">${app.email}</span>
                        </div>
                        <div class="detail-row">
                            <span class="label">Phone:</span>
                            <span class="value">${app.phone}</span>
                        </div>
                        <div class="detail-row">
                            <span class="label">Location:</span>
                            <span class="value">${app.location}</span>
                        </div>
                    </div>
                    <div class="detail-section">
                        <h4><i class="fas fa-briefcase"></i> Professional Details</h4>
                        <div class="detail-row">
                            <span class="label">Position Applied:</span>
                            <span class="value">${app.position}</span>
                        </div>
                        <div class="detail-row">
                            <span class="label">Experience:</span>
                            <span class="value">${app.experience}</span>
                        </div>
                        <div class="detail-row">
                            <span class="label">Expected Salary:</span>
                            <span class="value">${app.currentSalary ? '₹' + app.currentSalary.toLocaleString() + '/month' : 'Not specified'}</span>
                        </div>
                        <div class="detail-row">
                            <span class="label">Qualification:</span>
                            <span class="value">${app.qualification}</span>
                        </div>
                    </div>
                    <div class="detail-section full-width">
                        <h4><i class="fas fa-file-pdf"></i> Resume</h4>
                        <div class="resume-info">
                            <span class="file-name"><i class="fas fa-paperclip"></i> ${app.resumeFileName}</span>
                            <a href="${API_URL}/api/career/applications/${app._id}/resume" target="_blank" class="btn-view-resume">
                                <i class="fas fa-external-link-alt"></i> Open in New Tab
                            </a>
                            <a href="${API_URL}/api/career/applications/${app._id}/resume" class="btn-view-resume" style="margin-left: 10px;" download="${app.resumeFileName}">
                                <i class="fas fa-download"></i> Download
                            </a>
                        </div>
                        <div class="resume-preview" style="margin-top: 15px;">
                            <div class="pdf-viewer-container">
                                <p style="padding: 30px; text-align: center; color: #64748b; background: #f8fafc; border-radius: 10px; border: 1px dashed #e2e8f0;">
                                    <i class="fas fa-file-pdf" style="font-size: 2rem; color: #ef4444; display: block; margin-bottom: 10px;"></i>
                                    <strong>${app.resumeFileName}</strong><br><br>
                                    <a href="${API_URL}/api/career/applications/${app._id}/resume" target="_blank" class="btn-view-resume" style="display: inline-flex;">
                                        <i class="fas fa-external-link-alt"></i> Click to View Resume
                                    </a>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="detail-section full-width">
                        <h4><i class="fas fa-file-alt"></i> Cover Letter</h4>
                        <div class="cover-letter-box">
                            ${app.coverLetter || '<em>No cover letter provided</em>'}
                        </div>
                    </div>
                    <div class="detail-section full-width">
                        <h4><i class="fas fa-clipboard-check"></i> Update Status</h4>
                        <div class="status-update-row">
                            <select id="careerStatusSelect" class="status-select">
                                <option value="new" ${app.status === 'new' ? 'selected' : ''}>New</option>
                                <option value="reviewed" ${app.status === 'reviewed' ? 'selected' : ''}>Reviewed</option>
                                <option value="shortlisted" ${app.status === 'shortlisted' ? 'selected' : ''}>Shortlisted</option>
                                <option value="interview-scheduled" ${app.status === 'interview-scheduled' ? 'selected' : ''}>Interview Scheduled</option>
                                <option value="selected" ${app.status === 'selected' ? 'selected' : ''}>Selected</option>
                                <option value="rejected" ${app.status === 'rejected' ? 'selected' : ''}>Rejected</option>
                            </select>
                            <button class="btn-primary" onclick="updateCareerStatus()">Update Status</button>
                        </div>
                        <div class="form-group" style="margin-top: 15px;">
                            <label>Notes:</label>
                            <textarea id="careerNotes" rows="3" placeholder="Add notes about this candidate...">${app.notes || ''}</textarea>
                        </div>
                    </div>
                </div>
            `;

            document.getElementById('downloadResumeBtn').href = app.resumeUrl;
            document.getElementById('careerDetailModal').classList.add('active');
        }
    } catch (error) {
        console.error('Error viewing career application:', error);
        alert('Error loading application details');
    }
}

async function updateCareerStatus() {
    try {
        const status = document.getElementById('careerStatusSelect').value;
        const notes = document.getElementById('careerNotes').value;

        const response = await fetch(`${API_URL}/api/career/applications/${currentCareerAppId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status, notes })
        });

        const data = await response.json();

        if (data.success) {
            alert('Status updated successfully!');
            closeCareerModal();
            loadCareerApplications();
        } else {
            alert(data.message || 'Error updating status');
        }
    } catch (error) {
        console.error('Error updating career status:', error);
        alert('Error updating status');
    }
}

async function deleteCareerApplication(id) {
    if (!confirm('Are you sure you want to delete this application?')) return;

    try {
        const response = await fetch(`${API_URL}/api/career/applications/${id}`, {
            method: 'DELETE'
        });

        const data = await response.json();

        if (data.success) {
            alert('Application deleted successfully!');
            loadCareerApplications();
        } else {
            alert(data.message || 'Error deleting application');
        }
    } catch (error) {
        console.error('Error deleting career application:', error);
        alert('Error deleting application');
    }
}

function closeCareerModal() {
    document.getElementById('careerDetailModal').classList.remove('active');
    currentCareerAppId = null;
}

// Open resume in new window/tab for viewing
function openResumeViewer(url, filename) {
    // For PDF files, open directly or use browser's PDF viewer
    if (filename.toLowerCase().endsWith('.pdf')) {
        window.open(url, '_blank');
    } else {
        // For DOC/DOCX, try Office Online viewer
        const officeViewerUrl = `https://view.officeapps.live.com/op/view.aspx?src=${encodeURIComponent(url)}`;
        window.open(officeViewerUrl, '_blank');
    }
}

// Career Filters
document.getElementById('applyCareerFiltersBtn')?.addEventListener('click', () => {
    const status = document.getElementById('careerStatusFilter').value;
    const position = document.getElementById('careerPositionFilter').value;

    let filtered = allCareerApplications;

    if (status) {
        filtered = filtered.filter(app => app.status === status);
    }

    if (position) {
        filtered = filtered.filter(app => app.position === position);
    }

    displayCareerApplications(filtered);
});

// ===========================
// Logout
// ===========================
document.getElementById('logoutBtn').addEventListener('click', () => {
    if (confirm('Are you sure you want to logout?')) {
        sessionStorage.removeItem('adminToken');
        sessionStorage.removeItem('adminUser');
        window.location.href = 'login.html';
    }
});

// ===========================
// User Management (Super Admin Only)
// ===========================

// Add User Button Event Listener
document.getElementById('addUserBtn')?.addEventListener('click', () => {
    document.getElementById('addUserModal').classList.add('active');
});

function closeAddUserModal() {
    document.getElementById('addUserModal').classList.remove('active');
    document.getElementById('addUserForm').reset();
}

async function createNewUser() {
    try {
        const fullName = document.getElementById('newUserFullName').value.trim();
        const email = document.getElementById('newUserEmail').value.trim();
        const phone = document.getElementById('newUserPhone').value.trim();
        const password = document.getElementById('newUserPassword').value;
        const role = document.getElementById('newUserRole').value;
        const status = document.getElementById('newUserStatus').value;

        // Validation
        if (!fullName || !email || !phone || !password) {
            alert('Please fill in all required fields');
            return;
        }

        if (password.length < 6) {
            alert('Password must be at least 6 characters');
            return;
        }

        const response = await fetch(`${API_URL}/api/admin/create-user`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                fullName,
                email,
                phone,
                password,
                role,
                status
            })
        });

        const data = await response.json();

        if (data.success) {
            alert('User created successfully!');
            closeAddUserModal();
            loadAdminUsers();
        } else {
            alert(data.message || 'Error creating user');
        }
    } catch (error) {
        console.error('Error creating user:', error);
        alert('Error creating user');
    }
}

async function loadAdminUsers() {
    if (currentAdmin.role !== 'super-admin') {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/api/admin/users`, {
            method: 'GET',
            headers: getAuthHeaders()
        });
        const data = await response.json();

        if (data.success) {
            allAdminUsers = data.data || [];
            updateUserStats();
            displayAllUsers();
        }
    } catch (error) {
        console.error('Error loading admin users:', error);
    }
}

function updateUserStats() {
    const total = allAdminUsers.length;
    const active = allAdminUsers.filter(u => u.status === 'active').length;
    const inactive = allAdminUsers.filter(u => u.status === 'inactive').length;

    document.getElementById('totalAdminUsers').textContent = total;
    document.getElementById('activeAdminUsers').textContent = active;
    document.getElementById('inactiveAdminUsers').textContent = inactive;
}

function displayAllUsers() {
    const tbody = document.getElementById('allUsersTable');

    if (allAdminUsers.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; padding: 20px;">No users found</td></tr>`;
        return;
    }

    tbody.innerHTML = allAdminUsers.map(user => {
        const isCurrentUser = user._id === currentAdmin.id;
        const statusClass = user.status === 'active' ? 'success' : 'danger';
        const statusIcon = user.status === 'active' ? 'check-circle' : 'ban';
        
        return `
        <tr>
            <td>${user.fullName} ${isCurrentUser ? '<span class="badge badge-info">You</span>' : ''}</td>
            <td>${user.email}</td>
            <td><span class="badge badge-${user.role === 'super-admin' ? 'purple' : 'blue'}">${user.role}</span></td>
            <td><span class="badge badge-${statusClass}"><i class="fas fa-${statusIcon}"></i> ${user.status}</span></td>
            <td>${new Date(user.createdAt).toLocaleDateString()}</td>
            <td>
                ${!isCurrentUser ? `
                    <button class="btn-${user.status === 'active' ? 'warning' : 'success'} btn-sm" onclick="toggleUserStatus('${user._id}', '${user.status}')">
                        <i class="fas fa-${user.status === 'active' ? 'ban' : 'check'}"></i> 
                        ${user.status === 'active' ? 'Deactivate' : 'Activate'}
                    </button>
                    <button class="btn-danger btn-sm" onclick="deleteUser('${user._id}')">
                        <i class="fas fa-trash"></i>
                    </button>
                ` : '<span class="text-muted">-</span>'}
            </td>
        </tr>
        `;
    }).join('');
}

async function toggleUserStatus(userId, currentStatus) {
    const action = currentStatus === 'active' ? 'deactivate' : 'activate';
    const newStatus = currentStatus === 'active' ? 'inactive' : 'active';
    if (!confirm(`Are you sure you want to ${action} this user?`)) return;

    try {
        const response = await fetch(`${API_URL}/api/admin/users/${userId}/status`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify({ status: newStatus })
        });
        const data = await response.json();

        if (data.success) {
            alert(`User ${action}d successfully!`);
            loadAdminUsers();
        } else {
            alert(data.message || `Failed to ${action} user`);
        }
    } catch (error) {
        console.error(`Error ${action}ing user:`, error);
        alert(`Failed to ${action} user`);
    }
}

async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user? This action cannot be undone.')) return;

    try {
        const response = await fetch(`${API_URL}/api/admin/users/${userId}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        const data = await response.json();

        if (data.success) {
            alert('User deleted successfully!');
            loadAdminUsers();
        } else {
            alert(data.message || 'Failed to delete user');
        }
    } catch (error) {
        console.error('Error deleting user:', error);
        alert('Failed to delete user');
    }
}

// ===========================
// Initialize
// ===========================
window.addEventListener('load', () => {
    // Set header user info
    if (currentAdmin.fullName) {
        document.getElementById('headerUserName').textContent = currentAdmin.fullName;
    }
    if (currentAdmin.role) {
        document.getElementById('headerUserRole').textContent = 
            currentAdmin.role === 'super-admin' ? 'Super Administrator' : 'Administrator';
    }

    // Hide super-admin only elements if not super admin
    if (currentAdmin.role !== 'super-admin') {
        document.querySelectorAll('.super-admin-only').forEach(el => {
            el.style.display = 'none';
        });
    }

    // Check if there's a page in the URL hash
    const page = window.location.hash.substring(1) || 'dashboard';
    
    // Prevent non-super-admins from accessing users page
    if (page === 'users' && currentAdmin.role !== 'super-admin') {
        navigateToPage('dashboard');
    } else {
        navigateToPage(page);
    }
});
