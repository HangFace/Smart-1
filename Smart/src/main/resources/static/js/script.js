console.log("this is js file");
const toggleSidebar = () => {
    if ($(".sidebar").is(":visible")) {
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};

const paymentStart = () => {
    console.log("payment started",);
    //  alert("amount is required");
    var amount = document.querySelectorAll('#payment_field')[0].value;
    console.log(amount);
    if (amount == "" || amount == null) {
        swal("Fail", "amount is required", "error");
        return;
    }
    if (amount == 0) {
        alert("Amount should be more than Zero");
        return;
    }
    $.ajax({
        url: "/user/create_order",
        data: JSON.stringify({amount: amount, info: 'order_request'}),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            console.log(response)
            if (response.status == "created") {
                let option = {
                    key: 'rzp_test_ZuqHK4IeVlDUDQ',
                    amout: response.amount,
                    currency: 'INR',
                    name: 'Dhruv',
                    description: 'donation',
                    imge: 'https://cdn.searchenginejournal.com/wp-content/uploads/2022/06/image-search-1600-x-840-px-62c6dc4ff1eee-sej-760x400.png',
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.rezorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        console.log('payment successfull');
                        updatePaymentOnServer(
                            response.razorpay_payment_id,
                            response.razorpay_order_id,
                            "paid"
                        );


                    },
                    prefill: {
                        "name": "Dhruv Vyas",
                        "email": "dvyas@gmail.com",
                        "contact": "9974752320"
                    },
                    note: {
                        "address": "Maninagar"
                    },
                    theme: {
                        "color": "#3399cc"
                    }
                };
                let rzp = new Razorpay(option);
                rzp.on('payment.failed', function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    swal("Failed", "Payment Fail", "fail");
                });
                rzp.open()

            }
        },
        error: function (error) {
            console.log(error)
            alert("Something went wrong")
        },
    });
};

function updatePaymentOnServer(payment_id, order_id, status)
{
    $.ajax({
        url: "/user/update_order",
        data: JSON.stringify({payment_id: payment_id, order_id: order_id, status: status}),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success:function(response){
            swal("Greate Job", "Payment done", "success");
        },
        error: function(error){
            swal("Fail", "Payment is successful but did not get on server", "error");
        }
    })
}

const search = () => {
    //console.log("searching");
    let name = $("#search-input").val();
    let phone    = $("#search-input").val();
    if(name == ""){
        $(".search-result").hide();
    }
    else{
        console.log(name);
        //sending request to server
        let url = `http://localhost:8080/search/${name}/${phone}`;
        fetch(url).then((response) =>{
           return response.json();
        }).then((data) => {
            let text = `<div class="list-group">`;
            data.forEach((contact) => {
                text += `<a href="/user/update-contact/${contact.cId}" class="list-group-item list-group-action"> ${contact.name}</a>`;
            })
            text +=`</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        });

    }
}


