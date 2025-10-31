// Sincronizar input number com range slider
const quantityInput = document.getElementById('quantity');
const quantityRange = document.getElementById('quantityRange');
const rangeValue = document.querySelector('.range-value');

if (quantityInput && quantityRange) {
    // Ajustar valor inicial do slider baseado no máximo permitido
    const maxValue = parseInt(quantityRange.getAttribute('max'));
    const initialValue = Math.min(10, maxValue);
    quantityInput.value = initialValue;
    quantityRange.value = initialValue;
    rangeValue.textContent = initialValue;
    
    quantityRange.addEventListener('input', function() {
        quantityInput.value = this.value;
        rangeValue.textContent = this.value;
    });

    quantityInput.addEventListener('input', function() {
        const value = Math.min(parseInt(this.value), parseInt(this.getAttribute('max')));
        this.value = value;
        quantityRange.value = value;
        rangeValue.textContent = value;
    });
}

// Event listeners para os botões de seleção de empresa
document.addEventListener('DOMContentLoaded', function() {
    // Adicionar event listeners para todos os botões de seleção
    const selectButtons = document.querySelectorAll('.select-btn');
    
    selectButtons.forEach(button => {
        button.addEventListener('click', function() {
            const companyId = this.getAttribute('data-company-id');
            const companyName = this.getAttribute('data-company-name');
            selectCompany(companyId, companyName);
        });
    });
    
    // Animação de entrada para cards
    const cards = document.querySelectorAll('.company-card, .info-card');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '0';
                entry.target.style.transform = 'translateY(20px)';
                
                setTimeout(() => {
                    entry.target.style.transition = 'all 0.5s ease';
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }, 100);
                
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });
    
    cards.forEach(card => observer.observe(card));
});

// Função para selecionar empresa
function selectCompany(companyId, companyName) {
    const companyIdInput = document.getElementById('companyId');
    const companyIdDisplay = document.getElementById('companyIdDisplay');
    
    if (companyIdInput && companyIdDisplay) {
        companyIdInput.value = companyId;
        companyIdDisplay.value = companyName;
        
        // Scroll suave para o formulário
        document.querySelector('.trading-section').scrollIntoView({ 
            behavior: 'smooth',
            block: 'center'
        });
        
        // Destacar temporariamente o campo
        companyIdDisplay.style.background = '#FFD700';
        companyIdDisplay.style.transition = 'background 0.5s ease';
        
        setTimeout(() => {
            companyIdDisplay.style.background = 'white';
        }, 500);
    }
}
