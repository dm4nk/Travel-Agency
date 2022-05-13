function PageTransitions() {
    //Email
    emailjs.init('4ibVH95AjQ9oAtQBz');
    var submitBtn = document.querySelector('.submit-btn');
    const form = document.querySelector('.contact-form');
    const inputs = document.querySelectorAll('#user_name, #user_email, #subject, #message');
    submitBtn.addEventListener('click', function (event) {
        event.preventDefault();
        emailjs.sendForm('service_61u2pz6', 'template_t0om7ye', form)
            .then(function () {
                console.log('SUCCESS!');
            }, function (error) {
                console.log('FAILED...', error);
            });
        inputs.forEach(input => {
            input.value = '';
        });
    });
}

//as vvadin has some DOM problems, using timeout
function  addListenersDelayed(){
    setTimeout( function(){
        PageTransitions();
    }, 1000)    
}; 

addListenersDelayed();