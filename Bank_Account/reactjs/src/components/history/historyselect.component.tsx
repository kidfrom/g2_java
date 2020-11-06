import React from "react";
import getTransactions, {
  getTransactionsPathVariable,
  TransactionModel,
} from "./history.service";
import styles from "./history.module.css";
import { NavLink, useParams } from "react-router-dom";

interface HistorySelectInterface {
  setTransactions: React.Dispatch<React.SetStateAction<TransactionModel[]>>;
  setIsTransactionsShown: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function HistorySelect({
  setTransactions,
  setIsTransactionsShown,
}: HistorySelectInterface) {
  const Account_Number = sessionStorage.getItem("Account_Number");
  const ISO_4217 = sessionStorage.getItem("ISO_4217");

  const lang = {
    ID: {
      Intro: {
        Period: "SILAKAN PILIH PERIODE TRANSAKSI YANG AKAN DILIHAT",
        TransactionType: "SILAKAN PILIH JENIS TRANSAKSI YANG AKAN DILIHAT",
      },
      TransactionType: {
        All: "Semua Transaksi",
      },
      Note: "Catatan : Histori transaksi maksimal 31 hari yang lalu.",
    },
  };

  const today = new Date().toISOString().split("T")[0]; // yyyy-mm-dd
  const [state, setState] = React.useState<{
    StartCalendar: string | null;
    EndCalendar: string | null;
  }>({
    StartCalendar: null,
    EndCalendar: null,
  });

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    setState({ ...state, [e.target.name]: e.target.value });
  }

  const [isHistoryDisabled, setIsHistoryDisabled] = React.useState(true);

  const { Start, End } = useParams<getTransactionsPathVariable>();
  React.useEffect(() => {
    function handleGetTransactions({
      Start,
      End,
    }: getTransactionsPathVariable) {
      getTransactions({ Start, End }).then((data) => {
        data.transactions.forEach((transaction, index) => {
          if (index === 0) {
            data.transactions[index].Ending_Balance = data.Opening_Balance;
          }
          if (Account_Number === data.transactions[index].Source) {
            data.transactions[index].Ending_Balance =
              data.transactions[Math.max(0, index - 1)].Ending_Balance -
              transaction.transaction_Value;
          } else if (Account_Number === data.transactions[index].Destination) {
            data.transactions[index].Ending_Balance =
              data.transactions[Math.max(0, index - 1)].Ending_Balance +
              transaction.transaction_Value;
          }
        });
        setTransactions(data.transactions);
        setIsTransactionsShown(true);
      });
    }

    const DatePattern = new RegExp("[0-9]{4}-[0-9]{2}-[0-9]{2}");
    if (
      DatePattern.test(Start) &&
      DatePattern.test(End) &&
      parseInt(End.split("-")[2]) - parseInt(Start.split("-")[2]) >= 0
    ) {
      /**
       * bug prevention, user manually request for example:
       * 1. /2020-11-2/2020-11-6
       * 2. /2020-11-06/2020-11-05
       */
      handleGetTransactions({ Start, End });
    }
  }, [Start, End, setTransactions, setIsTransactionsShown, Account_Number]);

  return (
    <>
      <p>{ISO_4217 === "360" && lang.ID.Intro.Period}</p>
      <div>
        <input
          type="radio"
          id="today"
          name="inputhistory"
          value="today"
          checked={isHistoryDisabled}
          onChange={() => setIsHistoryDisabled(!isHistoryDisabled)}
        />
        <label htmlFor="today">Hari Ini</label>
      </div>
      <div className={styles.flex}>
        <div className={styles.f1}>
          <input
            type="radio"
            id="history"
            name="inputhistory"
            value="history"
            checked={!isHistoryDisabled}
            onChange={() => setIsHistoryDisabled(!isHistoryDisabled)}
          />
          <label htmlFor="history">Histori</label>
        </div>
        <div className={styles.f1}>
          <label>Dari Tanggal</label>
        </div>
        <div className={styles.f1}>
          <label>Sampai Tanggal</label>
        </div>
      </div>
      <div className={styles.flex}>
        <div className={styles.f1} />
        <input
          type="date"
          className={styles.f1}
          disabled={isHistoryDisabled}
          name="StartCalendar"
          onChange={(e) => handleChange(e)}
        />
        <input
          type="date"
          className={styles.f1}
          disabled={isHistoryDisabled}
          name="EndCalendar"
          onChange={(e) => handleChange(e)}
        />
      </div>
      <p>{ISO_4217 === "360" && lang.ID.Intro.TransactionType}</p>
      <div>
        <input
          type="radio"
          id="all"
          name="inputTransactionType"
          checked
          readOnly
        />
        <label htmlFor="all">
          {ISO_4217 === "360" && lang.ID.TransactionType.All}
        </label>
      </div>
      <p>{ISO_4217 === "360" && lang.ID.Note}</p>
      <NavLink
        to={`/history/${
          state.StartCalendar !== null ? state.StartCalendar : today
        }/${state.EndCalendar !== null ? state.EndCalendar : today}`}
      >
        <button className={styles.mright}>Kirim</button>
      </NavLink>
    </>
  );
}
