import React, { Fragment } from 'react';
import { Listbox, Transition } from '@headlessui/react';
import { ChevronUpDownIcon, CheckIcon } from '@heroicons/react/20/solid';

export default function CustomDropdown({ value, onChange, options, disabled, placeholder }) {
  const selectedOption = options.find((opt) => opt.nodeId === value) || null;

  return (
    <div className="relative w-full">
      <Listbox value={value || ''} onChange={onChange} disabled={disabled}>
        {({ open }) => (
          <div className="relative mt-1">
            <Listbox.Button 
              className={`cursor-pointer focus:outline-none w-full px-4 py-3 text-left rounded-xl text-sm transition-all shadow-md border 
                ${disabled ? 'opacity-50 cursor-not-allowed bg-white/5 border-white/10 text-gray-500' : 'bg-gray-900 border-purple-500 text-white hover:border-purple-400 focus:ring-2 focus:ring-purple-500'}
              `}
            >
              <span className="block truncate">
                {selectedOption ? (
                  <span>
                    <span className="font-bold text-white">{selectedOption.nodeId}</span>
                    <span className="ml-2 text-xs text-gray-400 hidden sm:inline-block">PUBKEY-{selectedOption.publicKey?.slice(0, 8)}...</span>
                  </span>
                ) : (
                  <span className="text-gray-400">{placeholder}</span>
                )}
              </span>
              <span className="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                <ChevronUpDownIcon
                  className="h-5 w-5 text-gray-400"
                  aria-hidden="true"
                />
              </span>
            </Listbox.Button>
            
            <Transition
              show={open}
              as={Fragment}
              leave="transition ease-in duration-100"
              leaveFrom="opacity-100"
              leaveTo="opacity-0"
            >
              <Listbox.Options className="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-gray-800 py-1 shadow-lg border border-purple-500 ring-1 ring-black/5 focus:outline-none text-sm">
                {options.length === 0 ? (
                   <div className="relative cursor-default select-none py-3 px-4 text-gray-500 text-center text-xs">No nodes available</div>
                ) : (
                  options.map((node, index) => (
                    <Listbox.Option
                      key={node.nodeId || index}
                      className={({ active, selected }) =>
                        `relative cursor-pointer select-none py-3 pl-10 pr-4 transition-colors ${
                          selected ? 'bg-blue-600 text-white font-bold' : active ? 'bg-purple-600 text-white' : 'text-gray-300 hover:bg-purple-600/50'
                        }`
                      }
                      value={node.nodeId}
                    >
                      {({ selected, active }) => (
                        <>
                          <div className="flex flex-col">
                             <span className={`block truncate ${selected ? 'font-bold text-white' : 'font-medium'}`}>
                                {node.nodeId}
                             </span>
                             <span className={`block truncate text-xs mt-0.5 ${selected || active ? 'text-blue-200' : 'text-gray-500'}`}>
                                PUBKEY-{node.publicKey?.slice(0, 12)}...
                             </span>
                          </div>
                          {selected ? (
                            <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-white">
                              <CheckIcon className="h-5 w-5" aria-hidden="true" />
                            </span>
                          ) : null}
                        </>
                      )}
                    </Listbox.Option>
                  ))
                )}
              </Listbox.Options>
            </Transition>
          </div>
        )}
      </Listbox>
    </div>
  );
}
